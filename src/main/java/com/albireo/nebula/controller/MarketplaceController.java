package com.albireo.nebula.controller;

import com.albireo.nebula.dto.ItemDto;
import com.albireo.nebula.model.Item;
import com.albireo.nebula.model.Order;
import com.albireo.nebula.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MarketplaceController {
    private final MarketplaceService marketplaceService;

    @PostMapping("/marketplace/item")
    public Item postItem(@RequestBody ItemDto itemDto, @RequestHeader String sessionId) {
        return marketplaceService.registerItem(itemDto, sessionId);
    }

    @GetMapping("/marketplace/item/{itemId}")
    public Item getItem(@PathVariable Long itemId) {
        return marketplaceService.getItem(itemId);
    }

    @DeleteMapping("/marketplace/item/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader String sessionId) {
        marketplaceService.deleteItem(itemId, sessionId);
    }

    @GetMapping("/marketplace/items")
    public void getSellerItems(@RequestHeader String sessionId) {
        marketplaceService.getSellerItems(sessionId);
    }

    @GetMapping("/marketplace/search")
    public List<Item> searchItems(@RequestParam String type, @RequestParam String term) {
        return marketplaceService.searchItem(type, term);
    }

    @PostMapping("/marketplace/order_request")
    public Order createOrderRequest(@RequestHeader String sessionId, @RequestParam Long itemId, @RequestParam String message) {
        return marketplaceService.createOrder(itemId, message, sessionId);
    }

    @PutMapping("/markeplace/order_request/{requestId}?action={actionType}")
    public Order processOrderRequest(@PathVariable Long requestId, @PathVariable String actionType, @RequestHeader String sessionId) {
        return marketplaceService.handleOrderAction(requestId, actionType, sessionId);
    }

    @GetMapping("/marketplace/order_requests/seller?item={itemId}")
    public List<Order> getSellerOrderRequestList(@RequestHeader String sessionId) {
        return marketplaceService.getSellerOrders(sessionId);
    }

    @GetMapping("/marketplace/order_requests/buyer")
    public List<Order> getBuyerOrderRequestList(@RequestHeader String sessionId) {
        return marketplaceService.getBuyerOrders(sessionId);
    }
}
