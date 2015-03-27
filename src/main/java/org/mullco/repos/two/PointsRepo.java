package org.mullco.repos.two;

import thirdparty.FifaPointsService;

public class PointsRepo {

    public Integer getWinPoints() {
        return FifaPointsService.getWinPoints();
    }

    public Integer getTiePoints() {
        return FifaPointsService.getTiePoints();
    }

}
