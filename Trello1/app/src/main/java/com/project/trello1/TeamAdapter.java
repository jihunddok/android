package com.project.trello1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TeamAdapter extends BaseAdapter {

    ArrayList<TeamItem> teamItemList = new ArrayList<>();

    @Override
    public int getCount() {
        return teamItemList.size();
    }

    @Override
    public TeamItem getItem(int i) {
        return this.teamItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context mContext = viewGroup.getContext();
        viewHolder holder; // 추가한 xml에서 위젯들을 연결해주는 역할을 하는 메소드

        // 같은 모양인데 곂칠 경우 방지
        if (view == null) {
            holder = new viewHolder();

            // 레이아웃 위에 신규 레이아웃을 덮어씀
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // inflater 메소드를 활용하여 view 객체에 위젯들을 표현할 수 있도록 해줌
            view = inflater.inflate(R.layout.team_adapter, null);

            // xml의 위젯을 findViewById로 연결 - 각 리스트뷰 아이템의 변별력을 위해 해줌
            holder.tv_teamCode =view.findViewById(R.id.teamCode);
            holder.tv_teamName = view.findViewById(R.id.teamName);
            holder.tv_teamMemberNo = view.findViewById(R.id.teamMemberNo);
            view.setTag(holder);
        }
        else {
            holder = (viewHolder) view.getTag();
        }

        // 커스텀한 xml에 Parse한 데이터베이스 정보들을 성정해주는 코드를 작성함
        final TeamItem mData = getItem(i);

        // 위젯에 각 아이템에 맞는 데이터를 설정
        holder.tv_teamCode.setText(mData.getTeamCode());
        holder.tv_teamName.setText(mData.getTeamName());
        holder.tv_teamMemberNo.setText(mData.getTeamMemberNo());

        return view;
    }

    class viewHolder{
        public TextView tv_teamCode;
        public TextView tv_teamName;
        public TextView tv_teamMemberNo;
    }

    public void addItem(String _teamCode, String _teamName, String _teamMemberNo){
        TeamItem teamItem = new TeamItem(_teamCode, _teamName, _teamMemberNo);
        teamItemList.add(teamItem);
    }
}
