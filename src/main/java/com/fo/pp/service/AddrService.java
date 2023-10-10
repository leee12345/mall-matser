package com.fo.pp.service;

import com.fo.pp.dao.AddrDAO;
import com.fo.pp.pojo.po.Addr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddrService {
    @Autowired
    private AddrDAO addrDAO;

    public Addr[] saveAddr(String name,String mobile,String province,String city,String district,String addr,String zip,int user_id)
    {
        Addr address = new Addr();
        address.setName(name);
        address.setMobile(mobile);
        address.setProvince(province);
        address.setCity(city);
        address.setDistrict(district);
        address.setAddr(addr);
        address.setZip(zip);
        address.setUserId(user_id);
        address.setDel(0);
        address.setDeFault(0);
        if(addrDAO.insertAddr(address)== 0) {
            throw new RuntimeException("增加地址失败");
        }
        Addr ad[] = addrDAO.selectAllAddr();
        return ad;
    }
    public Addr[] updateAddr(int id,String name,String mobile,String province,String city,String district,String addr,String zip,int user_id)
    {
        if(addrDAO.updateAddr(id,name,mobile,province,city,district,addr,zip,user_id)== 0) {
            throw new RuntimeException("更新地址失败");
        }
        Addr ad[] = addrDAO.selectAllAddr();
        return ad;
    }
    public Addr[] delAddr(int id)
    {
        addrDAO.delAddr(id);
        Addr[] address = addrDAO.selectDelAddr();
        return address;
    }

    public Addr[] findAddr(int user_id)
    {
        Addr[] address = addrDAO.selectFindAddr(user_id);
        return address;
    }

    public Addr[] setDefaultAddr(int id,int user_id)
    {
        addrDAO.setDefaultAddr1(id,user_id);

        addrDAO.setDefaultAddr2(id,user_id);

        Addr[] address = addrDAO.selectFindAddr(user_id);
        return address;
    }

    public Addr findAddressById(int id)
    {
        Addr addr = addrDAO.selectAddrById(id);
        return addr;
    }
}
