package com.project.trello1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BoardAdapter extends BaseAdapter {

    ArrayList<BoardItem> boardItems = new ArrayList<>();

    @Override
    public int getCount() {
        return boardItems.size();
    }

    @Override
    public BoardItem getItem(int position) {
        return boardItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context mContext = viewGroup.getContext();
        BoardAdapter.viewHolder holder; // 추가한 xml에서 위젯들을 연결해주는 역할을 하는 메소드

        // 같은 모양인데 곂칠 경우 방지
        if (view == null) {
            holder = new BoardAdapter.viewHolder();

            // 레이아웃 위에 신규 레이아웃을 덮어씀
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // inflater 메소드를 활용하여 view 객체에 위젯들을 표현할 수 있도록 해줌
            view = inflater.inflate(R.layout.board_adapter, null);

            // xml의 위젯을 findViewById로 연결 - 각 리스트뷰 아이템의 변별력을 위해 해줌
            holder.tv_boardCode = view.findViewById(R.id.boardCode);
            holder.tv_boardName = view.findViewById(R.id.boardName);
            view.setTag(holder);
        }
        else {
            holder = (BoardAdapter.viewHolder) view.getTag();
        }

        // 커스텀한 xml에 Parse한 데이터베이스 정보들을 성정해주는 코드를 작성함
        final BoardItem mData = getItem(i);

        // 위젯에 각 아이템에 맞는 데이터를 설정
        holder.tv_boardCode.setText(mData.getBoardCode());
        holder.tv_boardName.setText(mData.getBoardName());

        return view;
    }

    class viewHolder{
        public TextView tv_boardCode;
        public TextView tv_boardName;
    }

    public void addItem(String _boardCode, String _boardName){
        BoardItem boardItem = new BoardItem(_boardCode, _boardName);
        boardItems.add(boardItem);
    }
}
