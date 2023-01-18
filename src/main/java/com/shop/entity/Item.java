package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity //이 클래스를 엔티티로 선언
@Table(name="item") //엔티티와 매핑할 테이블을 지정
@Getter
@Setter
@ToString
public class Item extends BaseEntity{

    @Id //테이블의 기본키에 사용할 속성을 지정
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO) //키값을 생성하는 전략 명시 .AUTO : JPA 구현체가 자동으로 생성 전략을 결정
    private Long id;

    @Column(nullable = false, length = 50) //필드와 컬럼을 매칭
    private String itemNm;

    @Column(name="price", nullable = false)
    private int price;

    @Column(nullable = false)
    private int stockNumber;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Column(nullable = false)
    private String itemCategory;

    @Enumerated(EnumType.STRING) //Enum 타입 매칭
    private ItemSellStatus itemSellStatus;

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.itemCategory = itemFormDto.getItemCategory();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;

        if(restStock < 0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량 : " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber) { this.stockNumber += stockNumber; }
}
