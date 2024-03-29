package com.hoarse.auction.web.repository.Auction;

import com.hoarse.auction.web.entity.auction.AuctionRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRoomRepository extends JpaRepository<AuctionRoom, Long> {

    AuctionRoom findByRoomId(String roomId);


}
