package br.com.virilcorp.frentelite.context;

public class ApplicationContext {
	
	private static final ApplicationContext instance;
	
	static{
		instance = new ApplicationContext();
	}

	@SuppressWarnings("unchecked")
	public <T> T getAtributoSessao(String name){
		return (T) Session.get(name);
	}
	
	public <T> T setAtributoSessao(String name, T value){
		Session.put(name, value);
		return value;
	}
	
	public static ApplicationContext getInstance(){
		return instance;
	}
}