package com.gablum.auction.auctions;


import com.gablum.auction.auctions.Auction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
public class AuctionServiceTests {

    @Mock
    private AuctionRepo auctionRepo;

    @InjectMocks
    private AuctionService auctionService;

    private Auction testAuction1 = new Auction();
    private Auction testAuction2 = new Auction();

    @BeforeEach
    public void setupEntities() {
        testAuction1.setProposalId(UUID.randomUUID());
//        testAuction1.setAuctionId(1);
        testAuction2.setProposalId(UUID.randomUUID());
//        testAuction2.setAuctionId(2);
    }

    @Test
    public void serviceCanStore() {
        Mockito.when(auctionRepo.saveAll(Mockito.any(List.class))).thenReturn(
                List.of(testAuction1, testAuction2)
        );

        Assertions.assertEquals(
                2,
                auctionService.addAuctions(List.of(testAuction1, testAuction2)).size(),
                "should be able to store all Auction objects"
        );
    }

    @Test
    public void serviceCanGetAll() {
        Mockito.when(auctionRepo.findAll()).thenReturn(
                List.of(testAuction1, testAuction2)
        );

        Assertions.assertEquals(
                2,
                auctionService.getAllAuctions().size(),
                "should be able to find all objects"
        );
    }

    @Test
    public void serviceCanGetById() {
        Mockito.when(auctionRepo.findByAuctionId(Mockito.any(UUID.class))).thenReturn(
                Optional.of(testAuction1)
        );

        Assertions.assertEquals(
                testAuction1.getAuctionId(),
                auctionService.getAuctionById(UUID.randomUUID()).getAuctionId(),
                "service should be able to get auction by id"
        );
    }
}
