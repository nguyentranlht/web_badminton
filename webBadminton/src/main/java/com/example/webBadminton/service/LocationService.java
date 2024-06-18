package com.example.webBadminton.service;

import com.example.webBadminton.model.Location;
import com.example.webBadminton.repository.ILocationRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class LocationService {
    @Autowired
    ILocationRepository locationRepository;

    public List<Location> getAll(){return locationRepository.findAll();}

    public List<Location> getAllByProvince(String province){return locationRepository.findLocations(province, null,null);}

    public List<Location> getAllByDistrict(String district){return locationRepository.findLocations(null, district, null);}

    public List<Location> getAllByWard(String ward){return locationRepository.findLocations(null, null, ward);}
}
