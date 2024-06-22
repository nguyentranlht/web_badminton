package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.modelView.SearchCriteria;
import com.example.webBadminton.repository.IBadmintonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private IBadmintonRepository badmintonRepository;

    public List<Badminton> search(SearchCriteria criteria) {
        // Implement search logic based on criteria
        // For example, a basic search might look like this:
        return null;
//                badmintonRepository.findByLocationAndTime(
//                criteria.getProvince(),
//                criteria.getDistrict(),
//                criteria.getWard(),
//                criteria.getDay(),
//                criteria.getStartTime(),
//                criteria.getEndTime());
    }
}
