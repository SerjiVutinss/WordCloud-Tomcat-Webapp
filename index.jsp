<%@ include file="includes/header.jsp" %>

<div class="animated bounceInDown" style="font-size:48pt; font-family:arial; color:#990000; font-weight:bold">Web
    Opinion Visualiser
</div>

</p>&nbsp;</p>&nbsp;</p>

<table width="600" cellspacing="0" cellpadding="7" border="0">
    <tr>
        <td valign="top">

            <form bgcolor="white" method="POST" action="doProcess">
                <fieldset>
                    <legend><h3>Specify Details</h3></legend>

<%--                    <b>Select Option:</b>--%>
<%--                    <p>--%>
<%--                        You should make the most of the ability to configure a search with different algorithms and--%>
<%--                        heuristics. You can employ fuzzy logic or machine--%>
<%--                        learning if you like.--%>
<%--                    <p>--%>

                    <p><b>Site to Search:</b>
                        <span>
                            <select name="cmbSearchSite">
                                <option selected>DuckDuckGo</option>
                                <option>Wikipedia</option>
                            </select>
                        </span>
                    </p>

                    <p><b>Search Type:</b>
                        <span>
                            <select name="cmbSearchType">
                                <option selected>Best First</option>
                                <option>Beam</option>
                            </select>
                        </span>
                    </p>

                    <p><b>Max Results:</b>
                        <span>
                            <select name="cmbMaxResults">
                                <option>5</option>
                                <option>10</option>
                                <option selected>20</option>
                                <option>32</option>
                                <option>50</option>
                                <option>100</option>
                            </select>
                        </span>
                    </p>

                    <p><b>Max Depth:</b>
                        <span>
                            <select name="cmbMaxDepth">
                                <option>5</option>
                                <option>10</option>
                                <option>20</option>
                                <option selected>50</option>
                                <option>100</option>
                                <option>200</option>
                            </select>
                        </span>
                    </p>

                    <p><b>Beam Width (if Beam search):</b>
                        <span>
                            <select name="cmbBeamWidth">
                                <option>3</option>
                                <option selected>5</option>
                                <option>10</option>
                            </select>
                        </span>
                    </p>

                    <p><b>Threshold (for fuzzy score):</b>
                        <span>
                            <select name="cmbThreshold">
                                <option>1</option>
                                <option>2</option>
                                <option>3</option>
                                <option selected>4</option>
                                <option>5</option>
                                <option>6</option>
                                <option>7</option>
                                <option>8</option>
                                <option>9</option>
                            </select>
                        </span>
                    </p>

                    <p><b>Polling Time:</b>
                        <span>
                            <select name="cmbPollingTime">
                                <option selected>1</option>
                                <option>3</option>
                                <option>5</option>
                                <option>10</option>
                                <option>20</option>
                                <option>30</option>
                            </select>
                        </span>
                    </p>

<%--                    <p>--%>
<%--                        Only use the following JARs with your application. You can assume that they have already been--%>
<%--                        added to the Tomcat CLASSPATH:--%>
<%--                    <p>--%>
<%--                    <ol>--%>
<%--                        <li><a href="https://jsoup.org">JSoup</a>--%>
<%--                        <li><a href="http://jfuzzylogic.sourceforge.net/html/index.html">JFuzzyLogic</a>--%>
<%--                        <li><a href="https://github.com/jeffheaton/encog-java-core">Encog</a>--%>
<%--                    </ol>--%>
<%--                    <p/>--%>

                    <b>Enter Text :</b><br>
                    <input name="strQuery" size="100">
                    <p/>

                    <center><input type="submit" value="Search & Visualise!"></center>
                </fieldset>
            </form>

        </td>
    </tr>
</table>
<%@ include file="includes/footer.jsp" %>

