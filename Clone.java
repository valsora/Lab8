import java.util.Locale;

public class Clone {
    private String id;
    private String aka;
    private String unit;
    private int kills;
    private int missions;
    //while transformation
    private String rank;
    private double KMRatio;

    public Clone(String id, String aka, String unit, int kills, int missions, String rank, double KMRatio) {
        this.id = id;
        this.aka = aka;
        this.unit = unit;
        this.kills = kills;
        this.missions = missions;
        this.rank = rank;
        this.KMRatio = KMRatio;
    }

    public String getId() {
        return id;
    }

    public String getAka() {
        return aka;
    }

    public String getUnit() {
        return unit;
    }

    public int getKills() {
        return kills;
    }

    public int getMissions() {
        return missions;
    }

    public String getRank() {
        return rank;
    }

    public double getKMRatio() {
        return KMRatio;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder("ID: " + id + ", AKA: " + aka + ", Unit: " + unit + ", Kills confirmed: " + kills + ", Missions completed: " + missions);
        if (rank != null) string.append(", Rank: " + rank);
        if (KMRatio != -1) string.append(", K/M ratio " + String.format(Locale.ROOT, "%.2f", KMRatio));
        return string.toString();
    }
}
