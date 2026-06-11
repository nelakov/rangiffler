package com.elakov.rangiffler.helper.comparator.report.html;

import com.elakov.rangiffler.helper.comparator.Line;
import com.elakov.rangiffler.helper.comparator.Text;
import com.elakov.rangiffler.helper.comparator.report.style.BrightTextStyle;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DiffReport {

    private static final String CSS = """
            <style type="text/css">
            	body{
            		margin:0;
            		padding:0;
            		height: 100%;
            		width: 100%;
            		color:rgb(50,50,50);
            		font-size:14px;
            		font-family:monospace;
            	}
            	.tbl{
                	display:table;
            	    width: 100%;
            	}
            	.row{
            	    display:table-row;
            	    width: 100%;
            	}
            	.row:nth-child(odd){
                    background-color:rgb(248, 248, 248);
                }
            	.head{
                    display:table-header-group;
                    background-color:rgb(200, 250, 200);
                    text-align:center;
                    font-size:18px;
                }
                .ignor{
                    padding:10px;
                }
            	.cell{
            		margin:5px;
            		padding:5px;
            		display:table-cell;
            		text-align:left;
                }
                .bright{
                    background-color:rgb(255,220,220);
                }
            </style>""";

    private final Set<String> ignoredPaths;

    public DiffReport(Set<String> ignoredPaths) {
        this.ignoredPaths = ignoredPaths;
    }

    public String asHtml(List<Line> actual, List<Line> expected) {
        return "<!DOCTYPE html><html>" +
                "<head><meta charset=\"UTF-8\">" +
                CSS +
                "</head>" +
                "<body>" +
                ignoredPathsBanner() +
                "<div class='tbl'>" +
                "<div class='head'>" +
                "<div class='cell'>Actual</div>" +
                "<div class='cell'>Expect</div>" +
                "</div>" +
                toRows(actual, expected) +
                "</div>" +
                "</body></html>";
    }

    private String ignoredPathsBanner() {
        if (ignoredPaths.isEmpty()) {
            return "";
        }
        return "<div class='ignor'> Ignoring: " + String.join("<br>", ignoredPaths) + "</div>";
    }

    private String toRows(List<Line> left, List<Line> right) {
        StringBuilder rows = new StringBuilder();
        Iterator<Line> leftIter = left.iterator();
        Iterator<Line> rightIter = right.iterator();
        while (leftIter.hasNext() || rightIter.hasNext()) {
            rows.append("<div class='row'>");
            appendCell(rows, leftIter);
            appendCell(rows, rightIter);
            rows.append("</div>");
        }
        return rows.toString();
    }

    private void appendCell(StringBuilder rows, Iterator<Line> lines) {
        rows.append("<div class='cell'>");
        if (lines.hasNext()) {
            Line line = lines.next();
            rows.append("&nbsp;&nbsp;".repeat(Math.max(0, line.indentSize())));
            rows.append(render(line.text()));
            if (line.hasTrailingComma()) {
                rows.append(",");
            }
        }
        rows.append("</div>");
    }

    private String render(Text text) {
        return text.isBright()
                ? new BrightTextStyle(text.content()).decorate()
                : text.content();
    }
}
