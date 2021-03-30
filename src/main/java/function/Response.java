package function;

public class Response {
	public String code;
	public String message;
	public Object data;
	public Response() {
		super();
	}
	
	public Response(String code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}
	
	public Response(String code, String message) {
		this.code = code;
		this.message = message;
	}
	
}