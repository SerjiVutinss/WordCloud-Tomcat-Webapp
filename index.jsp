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

                    <b>Select Option:</b>
                    <p>
                        You should make the most of the ability to configure a search with different algorithms and
                        heuristics. You can employ fuzzy logic or machine
                        learning if you like.

                    <p>

                    <table>
                        <tr>
                            <th>Site</th>
                            <th>Option</th>
                        </tr>

                        <tr>
                            <td>
                                <select name="cmbSearchSite">
                                    <option selected>DuckDuckGo</option>
                                    <option>Wikipedia</option>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <select name="cmbMaxResults">
                                    <option>5</option>
                                    <option>10</option>
                                    <option>20</option>
                                    <option selected>32</option>
                                    <option>50</option>
                                    <option>100</option>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <select name="cmbMaxDepth">
                                    <option>5</option>
                                    <option>10</option>
                                    <option>20</option>
                                    <option selected>50</option>
                                    <option>100</option>
                                    <option>200</option>
                                </select>
                            </td>
                        </tr>

                        <tr>
                            <td>
                                <select name="cmbPollingTime">
                                    <option selected>1</option>
                                    <option>3</option>
                                    <option>5</option>
                                    <option>10</option>
                                    <option>20</option>
                                    <option>30</option>
                                </select>
                            </td>
                        </tr>

                    </table>


                    <p>

                        Only use the following JARs with your application. You can assume that they have already been
                        added to the Tomcat CLASSPATH:
                    <p>
                    <ol>
                        <li><a href="https://jsoup.org">JSoup</a>
                        <li><a href="http://jfuzzylogic.sourceforge.net/html/index.html">JFuzzyLogic</a>
                        <li><a href="https://github.com/jeffheaton/encog-java-core">Encog</a>
                    </ol>


                    <p/>

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

