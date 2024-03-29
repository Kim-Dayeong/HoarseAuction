package com.hoarse.auction.web.controller.auction;


import com.hoarse.auction.web.entity.auction.AuctionRoom;
import com.hoarse.auction.web.service.auction.AuctionRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionRoomController {

    private final AuctionRoomService auctionRoomService;


    // 채팅 리스트 화면
    @GetMapping("/room")
    public String rooms(Model model) {
        return "/auction/room";
    }
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public List<AuctionRoom> room() {
        return auctionRoomService.findAllRoom();
    }
    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public AuctionRoom createRoom(
            @RequestParam String name,
                                  @RequestParam String hoarseId) {

        System.out.println("아이디!!!!!"+hoarseId);
        return auctionRoomService.createRoom(name, hoarseId);
    }

    @GetMapping("/login")
    public String login(){

        return "/token/login";
    }

    @GetMapping("/token")
    public String token(@AuthenticationPrincipal Long userId){

        String token = userId.toString();


        return token;
    }



    // 채팅방 입장 화면
    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable String roomId) {
        model.addAttribute("roomId", roomId);


        return "/auction/roomdetail";
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public AuctionRoom roomInfo(@PathVariable String roomId) {
        return auctionRoomService.findById(roomId);
    }
}
