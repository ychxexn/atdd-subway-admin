package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class LineStation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "up_station_id", foreignKey = @ForeignKey(name = "fk_line_station_to_up_station"))
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "down_station_id", foreignKey = @ForeignKey(name = "fk_line_station_to_down_station"))
    private Station downStation;

    private int distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "line_id", nullable = false)
    private Line line;

    protected LineStation() {

    }

    public LineStation(Station upStation, Station downStation, int distance, Line line) {
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
        this.line = line;
    }

    public void arrangeFirstLineStation(LineStation newLineStation) {
        this.downStation = newLineStation.upStation;
    }

    public void arrangeLastLineStation(LineStation newLineStation) {
        this.upStation = newLineStation.getDownStation();
    }

    public void arrangeInterLineStation(LineStation newLineStation) {
        this.upStation = newLineStation.downStation;
        this.distance -= newLineStation.distance;
    }

    public boolean canAddFirstLineStation(LineStation newLineStation) {
        if (isNotValidNewLineStation(newLineStation)) {
            return false;
        }

        if (isNotFirstLineStation()) {
            return false;
        }

        return newLineStation.downStation.getId().equals(this.downStation.getId());
    }

    public boolean canAddLastLineStation(LineStation newLineStation) {
        if (isNotValidNewLineStation(newLineStation)) {
            return false;
        }

        if (isNotLastLineStation()) {
            return false;
        }

        return newLineStation.upStation.getId().equals(this.upStation.getId());
    }

    public boolean canAddInterLineStation(LineStation lineStation) {
        if (!isInterLineStation()) {
            return false;
        }
        return this.upStation.getId().equals(lineStation.upStation.getId())
                || this.downStation.getId().equals(lineStation.downStation.getId());
    }

    private boolean isNotFirstLineStation() {
        return this.upStation != null || this.downStation == null;
    }

    private boolean isNotLastLineStation() {
        return this.upStation == null || this.downStation != null;
    }

    private boolean isInterLineStation() {
        return isNotFirstLineStation() && isNotLastLineStation();
    }

    public boolean isSameLineStation(LineStation newLineStation) {
        return isInterLineStation()
                && this.upStation.getId().equals(newLineStation.getUpStation().getId())
                && this.downStation.getId().equals(newLineStation.getDownStation().getId());
    }

    private boolean isNotValidNewLineStation(LineStation newLineStation) {
        return newLineStation.getUpStation() == null || newLineStation.getDownStation() == null;
    }

    public boolean isShorterThan(LineStation newLineStation) {
        return this.distance <= newLineStation.distance;
    }

    public Long getId() {
        return id;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public int getDistance() {
        return distance;
    }

    public Line getLine() {
        return line;
    }
}
