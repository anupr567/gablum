package com.gablum.auction.auctions;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("bids")
@Getter @Setter @AllArgsConstructor @ToString @NoArgsConstructor
public class BidDataEntity {

    private String bidId = UUID.randomUUID().toString();

    private String auctionId;

    private String userId;

    private Bid bid;

    private ScoreObject scoreObject;

    private String createdBy;

    private long rank;
    public String toStringContract(){
        return this.getBidId() + this.getAuctionId() + this.getCreatedBy() +
                this.getBid().toString()+ String.valueOf(this.scoreObject.getTotal());
    }

}
