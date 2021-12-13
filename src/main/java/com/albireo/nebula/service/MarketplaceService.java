package com.albireo.nebula.service;

import com.albireo.nebula.dto.ItemDto;
import com.albireo.nebula.model.Album;
import com.albireo.nebula.model.Item;
import com.albireo.nebula.model.Order;
import com.albireo.nebula.model.User;
import com.albireo.nebula.repository.ItemRepository;
import com.albireo.nebula.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MarketplaceService {

    private final AccountService accountService;
    private final AlbumService albumService;

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public Item registerItem(ItemDto itemDto, String sessionId) {
        User seller = accountService.getUserFromSessionId(sessionId);
        Album itemAlbum = albumService.findById(itemDto.getAlbumId());
        Item item = Item.builder()
                .album(itemAlbum)
                .media(itemDto.getMedia())
                .seller(seller)
                .description(itemDto.getDescription())
                .condition(itemDto.getCondition())
                .price(itemDto.getPrice())
                .build();
        return itemRepository.save(item);
    }

    public Item getItem(Long itemId) {
        return itemRepository.findItemByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + itemId));
    }

    public void deleteItem(Long itemId, String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        Item item = itemRepository.findItemByItemId(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + itemId));
        if (!(user.getUsername().equals(item.getSeller().getUsername())
                || user.getRole() == User.Role.ADMIN)) {
            throw new IllegalArgumentException("Not Authorized");
        }
        itemRepository.delete(item);
    }

    public List<Item> getSellerItems(String sessionId) {
        User seller = accountService.getUserFromSessionId(sessionId);
        return itemRepository.findItemsBySeller(seller);
    }

    public List<Item> searchItem(String type, String term) {
        switch (type) {
            case "title":
                return itemRepository.findItemsByAlbum_TitleIgnoreCaseContaining(term);
            case "artist":
                return itemRepository.findItemsByAlbum_ArtistIgnoreCaseContaining(term);
            case "seller":
                return itemRepository.findItemsBySeller_UsernameIgnoreCaseContaining(term);
            default:
                throw new IllegalArgumentException("Illegal search type");
        }
    }

    public Order createOrder(Long itemId, String message, String sessionId) {
        Item item = getItem(itemId);
        User buyer = accountService.getUserFromSessionId(sessionId);
        Order order = Order.builder()
                .item(item)
                .buyer(buyer)
                .message(message)
                .build();
        return orderRepository.save(order);
    }

    public Order getOrder(Long orderId, String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        Order order = orderRepository.findOrderByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + orderId));
        authOrderRead(order, user);
        return order;
    }

    public Order handleOrderAction(Long orderId, String actionType, String sessionId) {
        Order order = getOrder(orderId, sessionId);
        User user = accountService.getUserFromSessionId(sessionId);
        if (order.getItem().getSeller().getUsername().equals(user.getUsername())) {
            switch (actionType) {
                case "approve":
                    order.setStatus(Order.Status.IN_PROGRESS);
                    order.getItem().setStatus(Item.Status.ON_ORDER);
                    return order;
                case "deny":
                    order.setStatus(Order.Status.CLOSED);
                    return order;
                case "establish":
                    order.setStatus(Order.Status.COMPLETE);
                    order.getItem().setStatus(Item.Status.SOLD);
                    return order;
                default:
                    throw new IllegalArgumentException("Illegal action type");
            }
        } else if (order.getBuyer().getUsername().equals(user.getUsername())) {
            if ("deny".equals(actionType)) {
                order.setStatus(Order.Status.CLOSED);
                return order;
            } else {
                throw new IllegalArgumentException("Illegal action type");
            }
        } else {
            throw new IllegalArgumentException("Unknown error");
        }
    }

    public Order getBuyerOrderInItem(Long itemId, String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        Item item = getItem(itemId);
        return orderRepository.findOrderByItemAndBuyer(item, user).orElse(null);
    }

    public List<Order> getSellerOrdersInItem(Long itemId, String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        Item item = getItem(itemId);
        return orderRepository.findOrdersByItemAndItem_Seller(item, user);
    }

    public List<Order> getBuyerOrders(String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        return orderRepository.findOrdersByBuyer(user);
    }

    public List<Order> getSellerOrders(String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        return orderRepository.findOrdersByItem_Seller(user);
    }

    private void authOrderRead(Order order, User user) {
        if (order.getBuyer().getUsername().equals(user.getUsername())
                || order.getItem().getSeller().getUsername().equals(user.getUsername())
                || user.getRole() == User.Role.ADMIN) {
            return;
        } else {
            throw new IllegalArgumentException("Not Authorized");
        }
    }
}
