package com.shop.dto;

import com.shop.entity.Delivery;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Data
public class DeliveryDto {

    private Long id;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String deliveryName;

    @NotEmpty(message = "우편번호는 필수 입력 값입니다.")
    private String deliveryZipcode;

    @NotEmpty(message = "주소는 필수 입력 값입니다.")
    private String deliveryStreetAdr;

    private String deliveryDetailAdr;

    @NotEmpty(message = "배송요청은 필수 입력 값입니다.")
    private String deliveryDetail;

    @NotEmpty(message = "입금정보는 필수 입력 값입니다.")
    private String deliveryAccount;

    @NotEmpty(message = "입금주는 필수 입력 값입니다.")
    private String deliveryAccountName;

    @Pattern(regexp = "^(01[1|6|7|8|9|0])-(\\d{3,4})-(\\d{4})$",message = "010-xxxx-xxxx 양식으로 입력하여주세요")
    private String deliveryPhone;

    private List<Long> orderIdList = new ArrayList<>();

    private static ModelMapper modelMapper = new ModelMapper();

    public Delivery createDelivery() {

        return modelMapper.map(this, Delivery.class);
    }

    public static DeliveryDto of(Delivery delivery){
        return modelMapper.map(delivery, DeliveryDto.class);
    }
}
