package com.gablum.usermanagement.user.rabbit;

import com.gablum.usermanagement.user.controller.MailController;
import com.gablum.usermanagement.user.model.othermodels.Auction;
import com.gablum.usermanagement.user.model.othermodels.Proposal;
import com.gablum.usermanagement.user.model.othermodels.BidMessage;
import com.gablum.usermanagement.user.security.UserListService;
import com.gablum.usermanagement.user.services.UserManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
@Slf4j
@EnableBinding(IGmailRabbit.class)
public class GmailRabbitListener {

    @Autowired
    private MailController mailController;

    @Autowired
    private UserManagementService userCont;

    @Autowired
    private UserListService userListService;

    @StreamListener("newProposal")
    public void newProposal(Proposal proposal){
        log.info("proposal--->",proposal.toString());

        mailController.sendingProposalMail(proposal);
        userListService.postMessageToUserListChannel(proposal);
    }

    @StreamListener("newAuction")
    public void newAuction(Auction auction){
        mailController.sendingAuctionMail(auction);
    }

    @StreamListener("newBid")
    public void newBid(BidMessage bidMessage){
        mailController.sendingBidMail(bidMessage);
    }
}
