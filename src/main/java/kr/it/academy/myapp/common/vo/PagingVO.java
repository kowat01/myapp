package kr.it.academy.myapp.common.vo;

import org.springframework.stereotype.Component;

@Component
public class PagingVO {

    private int totalPage;     // 전체 페이지 수
    private int totalRows;     // 전체 데이터 수
    private int pagePerRows;   // 한 페이지당 보여줄 데이터 수
    private int currentPage;   // 현재 페이지

    private int totalBlock;    // 전체 블록 수
    private int nowBlock;      // 현재 블록
    private int blockSize;     // 블록당 페이지 수

    public void setData(int currentPage, int totalRows) {
        this.currentPage = currentPage;
        this.totalRows = totalRows;
        this.pagePerRows = 10;
        this.blockSize = 10;
    }

    public int getOffSet() {
        return this.currentPage * this.pagePerRows;
    }

    public int getCount() {
        return this.pagePerRows;
    }

    private void getTotalPage() {
        double val = (double) this.totalRows / this.pagePerRows;
        this.totalPage = (int) Math.ceil(val);
    }

    private void getNowBlock() {
        double val = (double) this.currentPage / this.blockSize;
        this.nowBlock = (int) Math.floor(val);
    }

    private void getTotalBlock() {
        double val = (double) this.totalPage / this.blockSize;
        this.totalBlock = (int) Math.ceil(val);
    }

    public String pageHtml() {
        StringBuilder html = new StringBuilder();

        getTotalPage();
        getTotalBlock();
        getNowBlock();

        html.append("<ul class='pagination'>"); // ✅ 전체 감싸는 ul

        int pageNum;
        String disabled;
        String active;

        // 처음
        disabled = (this.currentPage > 0) ? "" : "disabled";
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(0);'>처음</a>");
        html.append("</li>");

        // 이전
        disabled = (this.nowBlock > 0) ? "" : "disabled";
        pageNum = (this.nowBlock * this.blockSize) - 1;
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(" + pageNum + ");'>이전</a>");
        html.append("</li>");

        // 페이지 번호
        for (int i = 0; i < this.blockSize; i++) {
            pageNum = (this.nowBlock * this.blockSize) + i;
            if (this.totalPage == 0 || this.totalPage == (pageNum)) break;

            active = (pageNum == this.currentPage) ? "active" : "";

            html.append("<li class='page-item " + active + "'>");
            html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(" + pageNum + ");'>" + (pageNum + 1) + "</a>");
            html.append("</li>");

            if (this.totalPage == (pageNum + 1)) break;
        }

        // 다음
        disabled = ((this.nowBlock + 1) < this.totalBlock) ? "" : "disabled";
        pageNum = ((this.nowBlock + 1) * this.blockSize);
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(" + pageNum + ");'>다음</a>");
        html.append("</li>");

        // 마지막
        disabled = (this.totalPage > (this.currentPage + 1)) ? "" : "disabled";
        pageNum = this.totalPage - 1;
        html.append("<li class='page-item " + disabled + "'>");
        html.append("<a class='page-link' href='javascript:void(0);' onclick='movePage(" + pageNum + ");'>마지막</a>");
        html.append("</li>");

        html.append("</ul>"); // ✅ 닫는 ul

        return html.toString();
    }
}
