package com.aaronhuang.medintel.domain.model;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.aaronhuang.medintel.domain.model.enums.ReminderCadence;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Reminder schedule for a user's medication.
 */
@Entity
public class UserMedicationReminder {
    /**
     * Primary key for this reminder record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Owning user medication.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_medication_id", nullable = false)
    private UserMedication userMedication;

    /**
     * Reminder cadence (daily, every other day, weekly, biweekly).
     */
    @Enumerated(EnumType.STRING)
    private ReminderCadence cadence;

    /**
     * Days of week for weekly/biweekly reminders.
     */
    @ElementCollection // indicates this is a collection of basic types (DayOfWeek enum),creates a separate tablecr
    @CollectionTable(name = "user_medication_reminder_days", joinColumns = @JoinColumn(name = "reminder_id")) //defines table 
    @Column(name = "day_of_week") //names the column in the collection table that holds the DayOfWeek values
    @Enumerated(EnumType.STRING) //persists the enum as a string (not as 0, 1, etc.)
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    /**
     * Times of day for the reminder (e.g., morning + night).
     */
    @ElementCollection // indicates this is a collection of basic types (LocalTime), creates a separate table
    @CollectionTable(name = "user_medication_reminder_times", joinColumns = @JoinColumn(name = "reminder_id"))
    @Column(name = "time_of_day")
    private Set<LocalTime> times = new HashSet<>();

    /**
     * IANA time zone id for reminder scheduling (e.g., "America/New_York").
     */
    private String timeZone;

    /**
     * Anchor date used for every-other-day cadence (optional).
     */
    private LocalDate anchorDate;

    /**
     * Whether this reminder is active.
     */
    private boolean active;

    /**
     * Whether the reminder has been marked as taken.
     */
    private boolean intaked;

    /**
     * UTC timestamp when this record was created.
     */
    private Instant createdAt;

    /**
     * Default constructor for JPA.
     */
    protected UserMedicationReminder() {}

    /**
     * Creates a reminder schedule for a user medication.
     *
     * @param userMedication owning user medication
     * @param cadence reminder cadence
     * @param timeZone IANA time zone id
     */
    public UserMedicationReminder(UserMedication userMedication, ReminderCadence cadence, String timeZone) {
        this.userMedication = userMedication;
        this.cadence = cadence;
        this.timeZone = timeZone;
        this.active = true;
        this.intaked = false;
        this.createdAt = Instant.now();
        this.anchorDate = LocalDate.now();
    }

    public UUID getId() {
        return id;
    }

    public UserMedication getUserMedication() {
        return userMedication;
    }

    public void setUserMedication(UserMedication userMedication) {
        this.userMedication = userMedication;
    }

    public ReminderCadence getCadence() {
        return cadence;
    }

    public void setCadence(ReminderCadence cadence) {
        this.cadence = cadence;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Set<LocalTime> getTimes() {
        return times;
    }

    public void setTimes(Set<LocalTime> times) {
        this.times = times;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public LocalDate getAnchorDate() {
        return anchorDate;
    }

    public void setAnchorDate(LocalDate anchorDate) {
        this.anchorDate = anchorDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isIntaked() {
        return intaked;
    }

    public void setIntaked(boolean intaked) {
        this.intaked = intaked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
