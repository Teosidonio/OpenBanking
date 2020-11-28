package mz.co.standardbank.e_biller.client;


/**
 * @author - C816346 on 2020/09/16
 */
public class ClientDto {

	private String nib;
	private boolean active;

	public String getNib () {
		return nib;
	}

	public void setNib ( String nib ) {
		this.nib = nib;
	}

	public boolean isActive () {
		return active;
	}

	public void setActive ( boolean active ) {
		this.active = active;
	}
}
