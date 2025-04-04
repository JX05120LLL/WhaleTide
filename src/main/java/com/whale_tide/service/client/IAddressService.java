package com.whale_tide.service.client;


import com.whale_tide.dto.client.address.AddressRequest;
import com.whale_tide.dto.client.address.AddressResponse;
import java.util.List;

public interface IAddressService {

    // 获取收获地址列表
    List<AddressResponse> getAddressList();

    // 获取收获地址详情
    AddressResponse getAddressDetails(long id);

    //添加收获地址
    void addAddress(AddressRequest addressRequest);

    //修改收获地址
    void modifyAddress(AddressRequest addressRequest,long id);

    //删除收获地址
    void deleteAddress(long id);

    //设置默认收获地址
    void setDefaultAddress(long id);



}
