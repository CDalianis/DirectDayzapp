package com.digitaldetox.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(
        name = "daily_checkins",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_daily_checkins_plan_date",
                columnNames = {"detox_plan_id", "entry_date"}
        )
)
public class DailyCheckIn extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detox_plan_id", nullable = false)
    private DetoxPlan detoxPlan;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "total_screen_minutes", nullable = false)
    private Integer totalScreenMinutes;

    @Column(name = "social_media_minutes")
    private Integer socialMediaMinutes;

    @Column(name = "sleep_hours", precision = 4, scale = 2)
    private BigDecimal sleepHours;

    @Column(name = "focus_score")
    private Integer focusScore;

    @Column(name = "stress_level")
    private Integer stressLevel;

    @Column(name = "craving_level")
    private Integer cravingLevel;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "dailyCheckIn", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Attachment> attachments = new HashSet<>();

    @PrePersist
    public void initializeUuid() {
        if (uuid == null) {
            uuid = UUID.randomUUID();
        }
    }

    public void addAttachment(Attachment attachment) {
        attachments.add(attachment);
        attachment.setDailyCheckIn(this);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DailyCheckIn that)) return false;
        return Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUuid());
    }
}
