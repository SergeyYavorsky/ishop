<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method="html" version="4.0" encoding="UTF-8" indent="yes"/>
<xsl:decimal-format name="russian" decimal-separator="," grouping-separator="&#160;" NaN=""/>
<xsl:template match="/">
<html><head>
<title>Styling</title>
</head>
<body>
<table border="1">
<tr><th>Дата</th><th>Звонки отвечено</th><th>Звонки не отвечено</th><th>Кол-во заказов принято</th><th>Сумма заказов принятых</th><th>Кол-во заказов отгруженных</th><th>Сумма заказов отгруженных</th></tr>
<xsl:for-each select="ROWSET/ROW">
<tr>
<td><xsl:value-of select="DAY"/></td>
<td style="text-align: center"><xsl:value-of select="CNT_C"/></td>
<td style="text-align: center"><xsl:value-of select="CNT_MISSED"/></td>
<td style="text-align: center"><xsl:value-of select="CNT_O"/></td>
<td style="text-align: center"><xsl:value-of select="format-number(SUM_O,'#&#160;###', 'russian')"/></td>
<td style="text-align: center"><xsl:value-of select="CNT_T"/></td>
<td style="text-align: center"><xsl:value-of select="format-number(SUM_T,'#&#160;###', 'russian')"/></td>
</tr>
</xsl:for-each>
<tr><th>Всего:</th>
<th><xsl:value-of select="sum(/ROWSET/ROW/CNT_C)"/></th>
<th><xsl:value-of select="sum(/ROWSET/ROW/CNT_MISSED)"/></th>
<th><xsl:value-of select="sum(/ROWSET/ROW/CNT_O)"/></th>
<th><xsl:value-of select="format-number(sum(/ROWSET/ROW/SUM_O),'#&#160;###', 'russian')"/></th>
<th><xsl:value-of select="sum(/ROWSET/ROW/CNT_T)"/></th>
<th><xsl:value-of select="format-number(sum(/ROWSET/ROW/SUM_T),'#&#160;###', 'russian')"/></th></tr>
</table>
</body>
</html>
</xsl:template>
</xsl:stylesheet>