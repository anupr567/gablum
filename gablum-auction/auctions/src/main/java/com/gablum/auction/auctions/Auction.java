package com.gablum.auction.auctions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AccessLevel;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@ToString
@Document(collection = "auctions")
public class Auction {

    @Indexed(unique = true)
    @Setter(AccessLevel.NONE)
    private UUID auctionId = UUID.randomUUID();
    private UUID proposalId;
    private String auctionName;

    boolean isAuctionActive;

    private UUID participantsVerificationId;
    private List<String> selectedParticipantList; // usernames
    private List<UUID> bidIdList;

    private Date createdOn;
    private Date updatedOn;

    private String createdBy; // username
    private String updatedBy; // username

    private Date auctionStartDate;
    private Date auctionEndDate;

}