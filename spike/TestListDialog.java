import docs.oracle.com.example.ListDialog;

public class TestListDialog {
	public static void main(String[] args) {
		String result = ListDialog.showDialog(null, null, 
				"Quem sou Eu?", "Este sou eu", 
				new String[]{"a","b","c","d"}, null,null);
		System.out.println(result);
	}
}
