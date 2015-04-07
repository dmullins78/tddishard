package thirdparty;

import us.monoid.web.Resty;

import java.io.IOException;

public class FifaPointsService {
    public static Integer getWinPoints() {
        return getFifaValue("/win");
    }

    public static Integer getTiePoints() {
        return getFifaValue("/tie");
    }

    private static Integer getFifaValue(String uri) {
        String fifaUrl = FifaSettings.getUrl();

        if (fifaUrl == null) {
            throw new IllegalArgumentException("No Fifa URL Provided");
        }

        try {
            String points = new Resty().text(fifaUrl + uri).toString();
            System.out.println(uri + " points = " + points);

            return Integer.valueOf(points);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
