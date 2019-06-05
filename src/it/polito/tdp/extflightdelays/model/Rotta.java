package it.polito.tdp.extflightdelays.model;

public class Rotta {
	
	private Airport partenza;
	private Airport destinazione;
	private double distanzaMedia;
	
	public Rotta(Airport partenza, Airport destinazione, double distanzaMedia) {
		super();
		this.partenza = partenza;
		this.destinazione = destinazione;
		this.distanzaMedia = distanzaMedia;
	}

	public Airport getPartenza() {
		return partenza;
	}

	public void setPartenza(Airport partenza) {
		this.partenza = partenza;
	}

	public Airport getDestinazione() {
		return destinazione;
	}

	public void setDestinazione(Airport destinazione) {
		this.destinazione = destinazione;
	}

	public double getDistanzaMedia() {
		return distanzaMedia;
	}

	public void setDistanzaMedia(double distanzaMedia) {
		this.distanzaMedia = distanzaMedia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destinazione == null) ? 0 : destinazione.hashCode());
		result = prime * result + ((partenza == null) ? 0 : partenza.hashCode());
		return result;
	}

	@Override //due rotte sono uguali quando aeroporto di destinazione e di arrivo sono uguali
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rotta other = (Rotta) obj;
		if (destinazione == null) {
			if (other.destinazione != null)
				return false;
		} else if (!destinazione.equals(other.destinazione))
			return false;
		if (partenza == null) {
			if (other.partenza != null)
				return false;
		} else if (!partenza.equals(other.partenza))
			return false;
		return true;
	}
	
	

}
