package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.CourtId;
import com.example.webBadminton.repository.ICourtRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional

public class CourtService {
    @Autowired
    private final ICourtRepository courtRepository;

    public List<Court> getAllCourts() {
        return courtRepository.findAll();
    }

    public List<Court> getAllCourtsByIdBadminton(Long id) {
        return courtRepository.findByBadmintonId(id);
    }

//    public Optional<Court> getCourtById(Long badmintonId, Long courtId) {
//        CourtId id = new CourtId(badmintonId, courtId);
//        return courtRepository.findById(id);
//    }


    public void addCourt(Court court) {
        courtRepository.save(court);
    }

    @Transactional
    public void deleteCourt(Long badmintonId, Long courtId) {
        CourtId id = new CourtId(badmintonId, courtId);
        courtRepository.deleteById(id);
    }

    public Court getCourtById(Long badmintonId, Long courtId) {
        CourtId id = new CourtId(badmintonId, courtId);
        return courtRepository.findById(id).orElse(null);
    }

    public Optional<Court> getCourtById(CourtId id) {
        return courtRepository.findById(id);
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

    public void updateCourt(@NotNull Court court) {
        CourtId id = new CourtId(court.getBadmintonId(), court.getCourtId());
        Court existingCourt = courtRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(court.getDetails() + " does not exist."));
        /*existingCourt.setPrice(court.getPrice());
        existingCourt.setDescription(court.getDescription());
        existingCourt.setCategory(court.getCategory());*/
        courtRepository.save(existingCourt);
    }
}
