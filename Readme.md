
<b>Description:</b>

The application consists of a servlet annotated with URL-mappings, processing components, the main JSP view to present downloaded images, error pages, and a logged-out page.<br/><br/>

<b>Processing Components:</b>

<i>REST client:</i> service interfacing module, and related processing units.

<i>Parts Utility:</i>

Extracts the byte streams for the uploaded image, and comment into the Client Request object. The content-disposition header, and user-agent are used to obtain the file-name. (Servlet requires @MultiPartConfig)

<i>File Utility:</i>

Creates the client directory, writes the files to disk, and generates a list of image URL’s.

<i>Session Attributes:</i>

Assigns the objects required by the JSP-UI into the session. Depending on the object returned from the REST service, the lists are removed, extended, or reinitialized.

<i>Error Utility:</i>

All errors are thrown up to the servlet for handling. The servlet passes the exception to the utility to determine the next page to render. The utility also evaluates the enumerated error if a custom error object is returned. There are error pages for an internal Java exception, a TCP/IP connection problem, a 400 or 500 Http response code, and a developer error returned in the service object. The index page is returned if a service enumerated error corresponding to user-entry is evaluated.

<i>Zip Utility</i>

The utility is passed the real path to the picture folder, the user directory, and a list of file names obtained from the values of selected checkboxes. The util creates the zip file, and writes it disk, assigning the disk path as a property. The servlet writes the content-disposition header, and the file is written to the socket using the ServletOutputStream.
 
<b>The REST Client Components</b>

<b>Interfacing module</b>
Contains methods that correspond to the service endpoints – constructs the request URL with parameters
 
<b>Apache TCP/IP client</b>

<b>JAXB utility</b>

<b>Http Response Code Utility</b>

<b>Custom exceptions - connection and client</b>

If the Apache request is successful, the Response utility evaluates the status code. 

If the status is not within the 200 range, the response utility initializes an HTTP client exception object. The custom exception contains fields for the raw entity response, the content-type, the status code, and a text field describing the content.  The message contains parsed HTML content or is assigned from the text field. The error is then thrown up to the servlet.

If no errors are thrown, the module invokes the JAXB utility to deserialize the Http entity. The interfacing module extracts the JAXBElement inner class containing the array of record entites into a Java class. For each entity in the array, the byte array value is obtained from the JAXBElement byte array and added to a list. The list will be used by the file utility to write the image data to disk. The deserialized response is returned to the servlet.

If an error occurs at any component in the process – the Apache client, a bad status code, a JAXB binding error, the error is rethrown to the servlet for handling.

<b>Session Attributes</b>

A Session Attributes component is passed the REST interfacing component containing the extracted fields. The Attributes component uses the FileUtility to create the client directory, write the extracted list of byte arrays to disk, and to generate the list of Image URL’s.

<b>JSP</b>

The pages included the main view to present downloaded images, error pages, and a logged-out page.

A tabbed division containing a standard html file input control is used to upload image files.

The application is started with a meta-refresh tag in the HTML header of the index page that redirects the browser request to the controller servlet. The servlet mapping for the initial request sets the session variable required to show the Bootstrap modal login. The value is rendered in a hidden input control, and read by JavaScript. 

EL references the session lists to display the images and meta-data in an iteration tag with the list of entity objects as the data source. The index property of the tag’s status attribute is used to index into image URL array. 

