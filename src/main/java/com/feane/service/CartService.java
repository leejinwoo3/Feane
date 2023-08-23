package com.feane.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import com.feane.dto.CartDetailDto;
import com.feane.dto.CartDto;
import com.feane.dto.CartMenuDto;
import com.feane.dto.OrderDto;
import com.feane.entity.Cart;
import com.feane.entity.CartMenu;
import com.feane.entity.Member;
import com.feane.entity.Menu;
import com.feane.repository.CartMenuRepository;
import com.feane.repository.CartRepository;
import com.feane.repository.MemberRepository;
import com.feane.repository.MenuRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

	private final MenuRepository menuRepository;
	private final MemberRepository memberRepository;
	private final CartRepository cartRepository;
	private final CartMenuRepository cartMenuRepository;
	private final OrderService orderService;

	public Long addCart(CartMenuDto cartMenuDto, String email) {
		System.out.println("cartMenuDto.getMenuId(): " + cartMenuDto.getMenuId());
		Menu menu = menuRepository.findById(cartMenuDto.getMenuId()).orElseThrow(EntityNotFoundException::new);
		Member member = memberRepository.findByEmail(email);
		Cart cart = cartRepository.findByMemberId(member.getId());
		
		if (cart == null) {
			cart = Cart.createCart(member);
			cartRepository.save(cart);
		}
		
		CartMenu savedCartMenu = cartMenuRepository.findByMenuAndCart(menu, cart);

		if (savedCartMenu != null) {
			savedCartMenu.addCount(cartMenuDto.getCount());
			return savedCartMenu.getId();
		} else {
			CartMenu cartMenu = new CartMenu();
			
			cartMenu.setMenu(menu);
			cartMenu.setCart(cart);
			
			cartMenuRepository.save(cartMenu);
			return cartMenu.getId();
		}
	}

	@Transactional(readOnly = true)
	public List<CartDetailDto> getCartList(String email) {
		List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

		Member member = memberRepository.findByEmail(email);
		Cart cart = cartRepository.findByMemberId(member.getId());
		if (cart == null) {
			return cartDetailDtoList;
		}

		cartDetailDtoList = cartMenuRepository.findCartDetailDtoList(cart.getId());
		return cartDetailDtoList;
	}
	@Transactional(readOnly = true)
	public boolean validateCartMenu(Long cartMenuId, String email) {
		Member curMember =memberRepository.findByEmail(email);
		CartMenu cartMenu = cartMenuRepository.findById(cartMenuId).orElseThrow(EntityNotFoundException::new);
		Member savedMember = cartMenu.getCart().getMember();
		if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }

        return true;
	}

	public void updateCartMenuCount(Long cartMenuId, int count) {
		CartMenu cartMenu = cartMenuRepository.findById(cartMenuId).orElseThrow(EntityNotFoundException::new);
		cartMenu.updateCount(count);
		
	}
    public void deleteCartMenu(Long cartMenuId) {
    	CartMenu cartMenu = cartMenuRepository.findById(cartMenuId)
                .orElseThrow(EntityNotFoundException::new);
    	cartMenuRepository.delete(cartMenu);
    }
    public Long orderCartMenu(List<CartDto> cartDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();

        for (CartDto cartDto : cartDtoList) {
        	CartMenu cartMenu = cartMenuRepository
                            .findById(cartDto.getCartMenuId())
                            .orElseThrow(EntityNotFoundException::new);

            OrderDto orderDto = new OrderDto();
            orderDto.setMenuId(cartMenu.getMenu().getId());
            orderDto.setCount(cartMenu.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);
        for (CartDto cartDto : cartDtoList) {
        	CartMenu cartMenu = cartMenuRepository
                            .findById(cartDto.getCartMenuId())
                            .orElseThrow(EntityNotFoundException::new);
        	cartMenuRepository.delete(cartMenu);
        }

        return orderId;
    }


	
}