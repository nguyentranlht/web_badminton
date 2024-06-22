package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Location;
import com.example.webBadminton.model.location.District;
import com.example.webBadminton.model.location.Province;
import com.example.webBadminton.model.location.Ward;
import com.example.webBadminton.repository.location.ILocationRepository;
import com.example.webBadminton.repository.location.IDistrictRepository;
import com.example.webBadminton.repository.location.IProvinceRepository;
import com.example.webBadminton.repository.location.IWardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LocationService {
    @Autowired
    ILocationRepository locationRepository;
    @Autowired
    IProvinceRepository provinceRepository;
    @Autowired
    IDistrictRepository districtRepository;
    @Autowired
    IWardRepository wardRepository;

    public List<Location> getAll(){return locationRepository.findAll();}

    public List<Location> getAllByProvince(String province){return locationRepository.findLocations(province, null,null);}

    public List<Location> getAllByDistrict(String district){return locationRepository.findLocations(null, district, null);}

    public List<Location> getAllByWard(String ward){return locationRepository.findLocations(null, null, ward);}

    public List<Province> getAllProvince(){return provinceRepository.findAll();}

    public List<District> getAllDistrict(){return districtRepository.findAll();}

    public List<Ward> getAllWard(){return wardRepository.findAll();}

    public String getProvinceName(String provinceId){return provinceRepository.getReferenceById(provinceId).getName();}
    public String getDistrictName(String districtId){return districtRepository.getReferenceById(districtId).getName();}
    public String getWardName(String wardId){return wardRepository.getReferenceById(wardId).getName();}
}
