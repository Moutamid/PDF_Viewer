-MainActivity.java

Implement PDF file selection and viewing

- Add functionality to select PDF files from storage.
- Implement permissions handling for accessing external storage.
- Utilize FilePicker library for picking PDF files.
- Enable launching PDF viewing activity upon selecting a file.
- Ensure compatibility with Android 11 (Tiramisu) permission model.

-PDFActivity.java

Implement PDF loading functionality

- Load PDF file from the provided path.
- Utilize PDFViewer library to display the PDF content.
- Handle errors gracefully and clear stored path if loading fails.
