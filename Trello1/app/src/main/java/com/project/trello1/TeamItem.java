package com.project.trello1;

public class TeamItem {

    String teamCode;
    String teamName;
    String teamMemberNo;

    TeamItem(String _teamCode, String _teamName, String _teamMemberNo) {
        teamCode = _teamCode;
        teamName = _teamName;
        teamMemberNo = _teamMemberNo;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getTeamMemberNo() {
        return teamMemberNo;
    }
}
