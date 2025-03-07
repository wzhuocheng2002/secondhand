package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.example.common.enums.OrderStatusEnum;
import com.example.entity.Account;
import com.example.entity.Address;
import com.example.entity.Goods;
import com.example.entity.Orders;
import com.example.mapper.OrdersMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import javax.annotation.Resource;

import jdk.nashorn.internal.parser.Token;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * 订单信息业务处理
 **/
@Service
public class OrdersService {

    @Resource
    private OrdersMapper ordersMapper;
    @Resource
    GoodsService goodsService;
    @Resource
    AddressService addressService;

    /**
     * 新增
     */
    public void add(Orders orders) {
        Integer goodsId = orders.getGoodsId();
        Goods goods =goodsService.selectById(goodsId);
        orders.setGoodsName(goods.getName());
        orders.setGoodsImg(goods.getImg());
        orders.setSaleId(goods.getUserId());
        orders.setTotal(goods.getPrice());

        Integer addressId = orders.getAddressId();

        // ① 先检查 addressId 是否为空，避免 NullPointerException
        if (addressId == null) {
            throw new IllegalArgumentException("请选择收货地址");
        }

        Address address = addressService.selectById(addressId);
        if (address == null) {
            throw new IllegalArgumentException("收货地址不存在");
        }
        orders.setUserName(address.getUserName());
        orders.setAddress(address.getAddress());
        orders.setPhone(address.getPhone());

        Account currentUser = TokenUtils.getCurrentUser();
        orders.setUserId(currentUser.getId());//下单人id
        orders.setStatus(OrderStatusEnum.NOTPAY.value);//默认订单是待支付
        orders.setOrderNo(System.currentTimeMillis() + RandomUtil.randomNumbers(7));
        orders.setTime(DateUtil.now());


        ordersMapper.insert(orders);
    }

    /**
     * 删除
     */
    public void deleteById(Integer id) {
        ordersMapper.deleteById(id);
    }

    /**
     * 批量删除
     */
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            ordersMapper.deleteById(id);
        }
    }

    /**
     * 修改
     */
    public void updateById(Orders orders) {
        ordersMapper.updateById(orders);
    }

    /**
     * 根据ID查询
     */
    public Orders selectById(Integer id) {
        return ordersMapper.selectById(id);
    }

    /**
     * 查询所有
     */
    public List<Orders> selectAll(Orders orders) {
        return ordersMapper.selectAll(orders);
    }

    /**
     * 分页查询
     */
    public PageInfo<Orders> selectPage(Orders orders, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Orders> list = ordersMapper.selectAll(orders);
        return PageInfo.of(list);
    }

}