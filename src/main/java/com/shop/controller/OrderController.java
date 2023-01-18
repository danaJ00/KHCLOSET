package com.shop.controller;

import com.shop.dto.DeliveryDto;
import com.shop.dto.MemberFormDto;
import com.shop.dto.OrderDto;
import com.shop.dto.OrderHistDto;
import com.shop.entity.Delivery;
import com.shop.entity.Member;
import com.shop.entity.Qna;
import com.shop.service.DeliveryService;
import com.shop.service.MemberService;
import com.shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final MemberService memberService;

    private final DeliveryService deliveryService;

    @GetMapping(value = {"/post", "/post/{page}"})
    public String orderPost(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        MemberFormDto memberFormDto = memberService.getMypageList(principal.getName());

        model.addAttribute("deliveryDto",new DeliveryDto());
        model.addAttribute("memberFormDto", memberFormDto);
        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "order/orderItem";
    }

    @PostMapping(value = "/post")
    public String newDelivery(@Valid DeliveryDto deliveryDto, @RequestParam("orderId") Long orderId, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors()){
            return "order/orderItem";
        }
        try {
            orderService.changeOrder(orderId);
            deliveryService.saveDelivery(deliveryDto, orderId);
        } catch (Exception e){
            model.addAttribute("errorMessage","배송지 등록 중 에러가 발생하였습니다.");
            return "order/orderItem";
        }

        return "redirect:/";
    }

    @PostMapping("/order")
    public @ResponseBody ResponseEntity order(@RequestBody @Valid OrderDto orderDto, BindingResult bindingResult,
                                              Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();

            for(FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }

        String email = principal.getName();
        Long orderId;

        try{
            orderId = orderService.order(orderDto,email);
        }catch(Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/orders", "/orders/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);
        return "order/orderHist";
    }


    @PostMapping("/order/{orderId}/cancel")
    public @ResponseBody ResponseEntity cancelOrder(@PathVariable("orderId")Long orderId, Principal principal){

        if(!orderService.validateOrder(orderId, principal.getName())){
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = "/admin/order/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId){
        orderService.deleteOrder(orderId);
        return "redirect:/admin/orders";
    }

    @GetMapping(value = "/admin/orders")
    public String deliveryList(@PathVariable("page") Optional<Integer> page, Principal principal, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 4);
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        List<Delivery> deliveryList = deliveryService.findDelivery();
        model.addAttribute("deliveryList", deliveryList);
        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage",5);

        return "order/orderMng";
    }



    @GetMapping(value = "/order/delete/{orderId}")
    public String deleteOrders(@PathVariable("orderId") Long orderId){
        orderService.deleteOrder(orderId);
        return "redirect:/post";
    }
}

