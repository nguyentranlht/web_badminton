package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import com.example.webBadminton.repository.ICourtRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourtService {
    @Autowired
    private ICourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public Court getCourtById(CourtId courtId) {
        return courtRepository.findById(courtId).orElse(null);
    }

    public void addCourt(Badminton badminton) {
        for (long i = 1; i <= badminton.getCourtQuantity(); i++) {
            Court court = new Court();
            court.setBadmintonId(badminton.getId()); // Set badmintonId directly
            court.setCourtId(i); // Set courtId directly as part of the composite key
            court.setDetails("Mini court #" + i + " of " + badminton.getBadmintonName());
            courtRepository.save(court);
        }
    }

    public void saveCourt(Court court) {
        courtRepository.save(court);
    }

    @Transactional
    public void deleteCourt(CourtId courtId) {
        courtRepository.deleteById(courtId);
    }

}
