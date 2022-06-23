package com.example.woowa.voucher.entity;

import static lombok.AccessLevel.PROTECTED;

import com.example.woowa.voucher.enums.VoucherType;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voucher")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Voucher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private VoucherType voucherType;

  @Column(nullable = false)
  private Integer discountValue;

  @Column(nullable = false)
  private LocalDateTime expirationDate;

  @Column(columnDefinition = "TINYINT DEFAULT FALSE")
  private Boolean isUse;

  @Column(nullable = false, unique = true)
  private String code;

  public Voucher(VoucherType voucherType, Integer discountValue, LocalDateTime expirationDate) {
    assert voucherType.isValidAmount(discountValue);
    assert expirationDate != null;
    assert ! code.isBlank();
    this.voucherType = voucherType;
    this.discountValue = discountValue;
    this.expirationDate = expirationDate;
    this.code = UUID.randomUUID().toString();
  }

  public String getVoucherType() {
    return voucherType.toString();
  }

  public void setVoucherType(VoucherType voucherType) {
    this.voucherType = voucherType;
  }

  public void setDiscountValue(int discountValue) {
    assert voucherType.isValidAmount(discountValue);
    this.discountValue = discountValue;
  }

  public void setExpirationDate(LocalDateTime expirationDate) {
    assert expirationDate.isAfter(LocalDateTime.now());
    this.expirationDate = expirationDate;
  }

  public int getDiscountPrice(int price) {
    assert ! isUse && voucherType.isOkayToDiscount(price, discountValue) && expirationDate.isAfter(LocalDateTime.now());
    isUse = true;
    return voucherType.discount(price, discountValue);
  }

  public void cancelUse() {
      isUse = false;
  }
}
