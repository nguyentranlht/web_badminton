package com.example.webBadminton.service;

import com.example.webBadminton.model.court.Badminton;
import com.example.webBadminton.model.court.Court;
import com.example.webBadminton.model.court.Location;
import com.example.webBadminton.repository.IBadmintonRepository;
import lombok.RequiredArgsConstructor;
import com.example.webBadminton.repository.ICourtRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BadmintonService {
    @Autowired
    private final IBadmintonRepository badmintonRepository;

    @Autowired
    private final LocationService locationService;

    @Autowired
    private final CourtService courtService;

    @Autowired
    private ICourtRepository courtRepository;

    public List<Badminton> getAllBadmintons() {
        return badmintonRepository.findAll();
    }

    public Optional<Badminton> getBadmintonById(Long id) {
        return badmintonRepository.findById(id);
    }

    public void addBadminton(Badminton badminton) {
        badmintonRepository.save(badminton);
    }

    public void updateQuantity(Long badmintonId)
    {
        Badminton badminton = badmintonRepository.getReferenceById(badmintonId);
        badminton.setCourtQuantity(badminton.getCourtQuantity() - 1);
    }

    public void deleteBadminton(Long id) {
        badmintonRepository.deleteById(id);
    }

    public void saveBadminton(Badminton badminton) {
        badmintonRepository.save(badminton);
    }

    public void updateBadminton(@NotNull Badminton badminton) {
        Badminton existingBadminton = badmintonRepository.findById(badminton.getId())
                .orElseThrow(() -> new IllegalStateException("Badminton with ID " + badminton.getId() + " does not exist."));
        existingBadminton.setBadmintonName(badminton.getBadmintonName());
        badmintonRepository.save(existingBadminton);
    }

    public List<Badminton> getAvailableBadminton(String provinceId, String districtId, String wardId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Location> locations = locationService.getAllByLocation(provinceId, districtId, wardId);

        if (locations.isEmpty()) {
            return Collections.emptyList(); // No locations found
        }

        List<Badminton> allBadmintons = badmintonRepository.findByLocations(locations);
        return allBadmintons.stream()
                .filter(badminton -> isBadmintonAvailable(badminton.getId(), date, startTime, endTime))
                .collect(Collectors.toList());
    }

    private boolean isBadmintonAvailable(Long badmintonId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<Court> courts = courtRepository.findByBadmintonId(badmintonId);
        return courts.stream()
                .anyMatch(court -> courtService.getAvailableCourt(date, court.getCourtId(), badmintonId, startTime, endTime));
    }
}
