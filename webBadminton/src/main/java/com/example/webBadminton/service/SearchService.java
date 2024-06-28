package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.modelView.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SearchService {
    @Autowired
    private BadmintonService badmintonService;

    public List<Badminton> search(SearchCriteria criteria) {
        // Implement search logic based on criteria
        // For example, a basic search might look like this:
        return badmintonService.getAvailableBadminton(
                criteria.getProvince(),
                criteria.getDistrict(),
                criteria.getWard(),
                criteria.getDay(),
                criteria.getStartTime(),
                criteria.getEndTime());
    }
}
