package com.example.woowa.customer.customer.service;

import com.example.woowa.customer.customer.converter.CustomerGradeConverter;
import com.example.woowa.customer.customer.dto.CustomerGradeCreateRequest;
import com.example.woowa.customer.customer.dto.CustomerGradeFindResponse;
import com.example.woowa.customer.customer.dto.CustomerGradeUpdateRequest;
import com.example.woowa.customer.customer.entity.CustomerGrade;
import com.example.woowa.customer.customer.repository.CustomerGradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomerGradeService {
    private final CustomerGradeRepository customerGradeRepository;

    @Transactional
    public CustomerGradeFindResponse createCustomerGrade(
        CustomerGradeCreateRequest customerGradeCreateRequest) {
        CustomerGrade customerGrade = CustomerGradeConverter.toCustomerGrade(
            customerGradeCreateRequest);
        customerGrade = customerGradeRepository.save(customerGrade);
        return CustomerGradeConverter.toCustomerGradeDto(customerGrade);
    }

    public CustomerGradeFindResponse findCustomerGrade(Long id) {
        CustomerGrade customerGrade = customerGradeRepository.findById(id).orElseThrow(() -> new RuntimeException("customer grade not existed"));
        return CustomerGradeConverter.toCustomerGradeDto(customerGrade);
    }

    @Transactional
    public CustomerGradeFindResponse updateCustomerGrade(Long id, CustomerGradeUpdateRequest updateCustomerGradeDto) {
        CustomerGrade customerGrade = customerGradeRepository.findById(id).orElseThrow(() -> new RuntimeException("customer grade not existed"));
        if (updateCustomerGradeDto.getGrade() != null) {
            customerGrade.setGrade(updateCustomerGradeDto.getGrade());
        }
        if (updateCustomerGradeDto.getOrderCount() != null) {
            customerGrade.setOrderCount(updateCustomerGradeDto.getOrderCount());
        }
        if (updateCustomerGradeDto.getDiscountPrice() != null) {
            customerGrade.setDiscountPrice(updateCustomerGradeDto.getDiscountPrice());
        }
        if (updateCustomerGradeDto.getVoucherCount() != null) {
            customerGrade.setVoucherCount(updateCustomerGradeDto.getVoucherCount());
        }
        return CustomerGradeConverter.toCustomerGradeDto(customerGrade);
    }

    public void deleteCustomerGrade(Long id) {
        CustomerGrade customerGrade = customerGradeRepository.findById(id).orElseThrow(() -> new RuntimeException("customer grade not existed"));
        customerGradeRepository.delete(customerGrade);
    }

    public CustomerGrade findDefaultCustomerGrade() {
        return customerGradeRepository.findFirstByOrderByOrderCount().orElseThrow(() -> new RuntimeException("no customer grade existed"));
    }

    public CustomerGrade findCustomerGradeByOrderPerMonthCount(int orderCount) {
        return customerGradeRepository.findFirstByOrderCountLessThanEqualOrderByOrderCountDesc(orderCount).orElse(findDefaultCustomerGrade());
    }
}
