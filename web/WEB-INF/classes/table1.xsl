<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes"/>
    <xsl:decimal-format name="russian" decimal-separator="," grouping-separator="&#160;" NaN=""/>
    <xsl:template match="t">
        <xsl:variable name="total_SUM_T" select="sum(table1/ROWSET/ROW/SUM_T)"/>
        <div class="ui-widget">
            <table class="ui-widget ui-widget-content" style="float:left" width="69%">
                <tr>
                    <th>Дата</th>
                    <th>Звонки отвечено</th>
                    <th>Звонки не отвечено</th>
                    <th>Кол-во заказов принято</th>
                    <th>Сумма заказов принятых</th>
                    <th>Кол-во заказов отгруженных</th>
                    <th>Сумма заказов отгруженных</th>
                </tr>
                <xsl:for-each select="table1/ROWSET/ROW">
                    <tr>
                        <td>
                            <xsl:value-of select="DAY"/>
                        </td>
                        <td style="text-align: center">
                            <xsl:value-of select="CNT_C"/>
                        </td>
                        <td style="text-align: center">
                            <xsl:value-of select="CNT_MISSED"/>
                        </td>
                        <td style="text-align: center">
                            <xsl:value-of select="CNT_O"/>
                        </td>
                        <td style="text-align: center">
                            <xsl:value-of select="format-number(SUM_O,'#&#160;###', 'russian')"/>
                        </td>
                        <td style="text-align: center">
                            <xsl:value-of select="CNT_T"/>
                        </td>
                        <td style="text-align: center">
                            <xsl:value-of select="format-number(SUM_T,'#&#160;###', 'russian')"/>
                        </td>
                    </tr>
                </xsl:for-each>
                <tr>
                    <th>Всего:</th>
                    <th>
                        <xsl:value-of select="sum(table1/ROWSET/ROW/CNT_C)"/>
                    </th>
                    <th>
                        <xsl:value-of select="sum(table1/ROWSET/ROW/CNT_MISSED)"/>
                    </th>
                    <th>
                        <xsl:value-of select="sum(table1/ROWSET/ROW/CNT_O)"/>
                    </th>
                    <th>
                        <xsl:value-of select="format-number(sum(table1/ROWSET/ROW/SUM_O),'#&#160;###', 'russian')"/>
                    </th>
                    <th>
                        <xsl:value-of select="sum(table1/ROWSET/ROW/CNT_T)"/>
                    </th>
                    <th>
                        <xsl:value-of select="format-number($total_SUM_T,'#&#160;###', 'russian')"/>
                    </th>
                </tr>
            </table>
            <table class="ui-widget ui-widget-content" style="float:right" width="29%">
                <tr>
                    <td>Кол-во отработанных дней</td>
                    <td>
                        <xsl:value-of select="table2/ROWSET/ROW/WORKED_DAYS"/>
                    </td>
                </tr>
                <tr class="ui-widget-header">
                    <td colspan="2">Выполнение</td>
                </tr>
                <tr>
                    <td>Факт за месяц</td>
                    <td>
                        <xsl:value-of select="format-number($total_SUM_T,'#&#160;###', 'russian')"/>
                    </td>
                </tr>
                <tr>
                    <td>Целевой план</td>
                    <td>
                        <xsl:value-of select="format-number(table2/ROWSET/ROW/TARGET,'#&#160;###', 'russian')"/>
                    </td>
                </tr>
                <tr>
                    <td>Прогноз на месяц</td>
                    <td>
                        <xsl:value-of
                                select="format-number($total_SUM_T div table2/ROWSET/ROW/WORKED_DAYS * table2/ROWSET/ROW/DAYS_IN_MONTH,'#&#160;###', 'russian')"/>
                    </td>
                </tr>
                <tr>
                    <td>% выполнения плана</td>
                    <td><xsl:value-of
                            select="format-number($total_SUM_T div table2/ROWSET/ROW/TARGET * 100,'#&#160;###', 'russian')"/>%
                    </td>
                </tr>
            </table>
        </div>
    </xsl:template>
</xsl:stylesheet>