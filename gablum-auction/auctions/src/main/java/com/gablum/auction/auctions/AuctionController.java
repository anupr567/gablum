package com.gablum.auction.auctions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gablum.auction.auctions.rabbit.BidMessage;
import com.gablum.auction.auctions.rabbit.StartAuctionBinding;
import com.gablum.auction.auctions.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AuctionController {

    @Autowired
    private AuctionService auctionService;

    @Autowired
    private BidService bidService;

    @Autowired
    private UserService userService;

    private MessageChannel messageChannelBid;
    private MessageChannel messageChannelAuction;
//    private MessageChannel messageChannelBid;

    public AuctionController(StartAuctionBinding auctionBinding) {
        this.messageChannelBid = auctionBinding.getNewBidTransmitChannel();
        this.messageChannelAuction = auctionBinding.floatingNewAuctionMessageChannel();
    }

    //FIXME: check roles before returning auction
    //FIXME: only allowed users (createdBy buyer/participating seller) can view details of auction
    @GetMapping("/auctions")
    @ResponseBody
    public List<Auction> getAllAuctions(
            @RequestParam Map<String, String> queryMap,
            HttpServletRequest request
    ) {
        String email = userService.getEmail(request);
        log.debug(email);
        List<Auction> auctionList = new ArrayList<Auction>();
        auctionList.addAll(auctionService.getAllAuctionsBuyer(queryMap, email));
        auctionList.addAll(auctionService.getAuctionSeller(queryMap, email));
        for(Auction auction: auctionList)  {
            auction.setSocketTokens(null);
        }
        return auctionList;
    }

    @GetMapping("/auctions/{id}")
    public Auction getAuctionById(@PathVariable("id") String auctionId) {
        Auction auction = auctionService.getAuctionById(auctionId);
        auction.setSocketTokens(null);
        return auction;
    }

    @PostMapping("/auctions")
    public List<Auction> addAuctions(@RequestBody List<Auction> auctionsToAdd, HttpServletRequest request) {
        int i = 0;
        String email = userService.getEmail(request);
        while (i < auctionsToAdd.size()){
            auctionsToAdd.get(i).setCreatedBy(email);

            log.info("email---------->" + email);
            if (auctionsToAdd.get(i).getSocketTokens() == null) {
                auctionsToAdd.get(i).setSocketTokens(new HashMap<String, String>());
            }
            auctionsToAdd.get(i).getSocketTokens().put(
                    email,
                    userService.generateToken(
                            email,
                            auctionsToAdd.get(i).getAuctionId(),
                            auctionsToAdd.get(i).getAuctionEndDate(),
                            true)
            );
            for (String sellerEmail: auctionsToAdd.get(i).getInterestedUsersEmail()) {
                auctionsToAdd.get(i).getSocketTokens().put(
                        sellerEmail,
                        userService.generateToken(
                                sellerEmail,
                                auctionsToAdd.get(i).getAuctionId(),
                                auctionsToAdd.get(i).getAuctionEndDate(),
                                false)
                );
            }
            i = i+1;
        }
        for(int j =0; j<auctionsToAdd.size(); j++){
            Message<Auction> msg = MessageBuilder.withPayload(auctionsToAdd.get(j)).build();
            messageChannelAuction.send(msg);
        }
        return auctionService.addAuctions(auctionsToAdd);
    }

    @GetMapping("auctions/seller")
    public List<Auction> getAllAuctionSeller( @RequestParam Map<String, String> queryMap,
                                             HttpServletRequest request){
        String email = userService.getEmail(request);
        log.debug(email);

        List<Auction> auctions =  auctionService.getAuctionSeller(queryMap, email);
        for(Auction auction: auctions)  {
            auction.setSocketTokens(null);
        }
        return auctions;
    }


    @PostMapping("auctions/{id}/bid/score")
    public ScoreObject getBidScore(@RequestBody Bid bid, @PathVariable String id) throws ParseException {
        ScoreObject scoreObject = new ScoreObject();
        scoreObject.setScore(bidService.getBidScore(bid, id));
        return scoreObject;
    }


    @PostMapping("auctions/{id}/bid")
    public ScoreObject addNewBid(@RequestBody Bid bid, @PathVariable String id, HttpServletRequest request) throws JsonProcessingException,
            ParseException, UnknownHostException {
        String email = userService.getEmail(request);
        float scorecnt = bidService.getBidScore(bid, id);
        BidDataEntity bidDataEntity = new BidDataEntity();
        bidDataEntity.setBid(bid);
        bidDataEntity.setScore(scorecnt);
        bidDataEntity.setCreatedBy(email);
        bidDataEntity.setAuctionId(id);

        bidService.addBid(bidDataEntity);

        Message<BidMessage> message = MessageBuilder.withPayload(
                new BidMessage(bidDataEntity, InetAddress.getLocalHost().getHostAddress())
        ).build();

        messageChannelBid.send(message);

        ScoreObject scoreObject = new ScoreObject();
        scoreObject.setScore(scorecnt);
        return scoreObject;
    }

    @GetMapping("/tokens/{auctionId}")
    public ResponseEntity<SocketToken> getAuctionSocketToken(
            HttpServletRequest request,
            @PathVariable("auctionId") String auctionId) {
        String email = userService.getEmail(request);
        Auction currentAuction = auctionService.getAuctionById(auctionId);
        String socketToken = currentAuction.getSocketTokens().get(email);
        if (socketToken == null) {
            return new ResponseEntity<SocketToken>(
                    new SocketToken(""),
                    HttpStatus.FORBIDDEN
            );
        }
        return new ResponseEntity<SocketToken>(
                new SocketToken(socketToken),
                HttpStatus.OK
        );
    }

    @GetMapping("auctions/{id}/bid")
    public List<BidDataEntity> bidDataEntityList( @RequestParam Map<String, String> queryMap, @PathVariable String id
            , HttpServletRequest request) {
        return bidService.getbidsAuction(queryMap, id);
    }

    @PutMapping("auction/{id}/bid/end")
    public Auction saveWinningBid(@PathVariable String id, @RequestBody BidDataEntity bidDataEntity,
                                   HttpServletRequest request){
        Auction auction = auctionService.getAuctionById(id);
        auction.setWinningBid(bidDataEntity.getBidId());
        auction.isAuctionFinished = true;

        return auctionService.updateAuction(auction);
    }

}
