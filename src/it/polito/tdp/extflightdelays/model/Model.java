package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private SimpleWeightedGraph<Airport, DefaultWeightedEdge> grafo;
	private Map<Integer,Airport> aIdMap;
	private ExtFlightDelaysDAO dao;
	Map<Airport, Airport> visita;
	
	public Model() {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		aIdMap = new HashMap<Integer, Airport>();
		dao = new ExtFlightDelaysDAO();
		dao.loadAllAirports(aIdMap);
		visita = new HashMap<Airport, Airport>();
	}
	
	public void creaGrafo(int distanzaMedia) {

		//Aggiungo i vertici
		Graphs.addAllVertices(grafo, aIdMap.values());
		
		//Posso recuperare gli archi, ciclando sulle rotte
		for(Rotta rotta : dao.getRotte(distanzaMedia, aIdMap)) {
			//controllo se esiste ià un arco tra i due vertici - se esiste, aggiorno il peso (faccio una nuova media).  
			DefaultWeightedEdge edge = grafo.getEdge(rotta.getPartenza(), rotta.getDestinazione());
			if (edge == null) {
				Graphs.addEdge(grafo, rotta.getPartenza(), rotta.getDestinazione(), rotta.getDistanzaMedia());
			} else {
				double peso = grafo.getEdgeWeight(edge);
				double newPeso = (peso + rotta.getDistanzaMedia())/2;
				grafo.setEdgeWeight(edge, newPeso);
			}
		}
		
		System.out.println("Vertici: " + grafo.vertexSet().size());
		System.out.println("Archi: " + grafo.edgeSet().size());
	}
	
	public Boolean testConnessione (Integer a1, Integer a2) {
		
		Set<Airport> visitati = new HashSet<Airport>(); //tengo traccia dei visitati
		
		Airport partenza = aIdMap.get(a1);
		Airport destinazione = aIdMap.get(a2);
		
		System.out.println("Test connessione tra " + partenza + " e " + destinazione);
		//visito il grafo in ampiezza a partire dall'aeroporto di partenza
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, partenza);
		
		while (it.hasNext()) {
			visitati.add(it.next());
		}
		
		if(visitati.contains(destinazione)) {
			return true;
		}else {
			return false;
		}
	}
	
	public List<Airport> trovaPercorso(Integer a1, Integer a2) {
		
		List<Airport> percorso = new ArrayList<Airport>();
		Airport partenza = aIdMap.get(a1);
		Airport destinazione = aIdMap.get(a2);
		
		System.out.println("Cerco percorso tra " + partenza + " e " + destinazione);
		
		//visito il grafo in ampiezza a partire dall'aeroporto di partenza
		BreadthFirstIterator<Airport, DefaultWeightedEdge> it = new BreadthFirstIterator<>(this.grafo, partenza);
		visita.put(partenza, null);
		it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> ev) {
				//58.00
				Airport sorgente = grafo.getEdgeSource(ev.getEdge());
				Airport destinazione = grafo.getEdgeTarget(ev.getEdge());
				
				if(!visita.containsKey(destinazione) && visita.containsKey(sorgente)) {
					visita.put(destinazione, sorgente);
				}else if (!visita.containsKey(sorgente) && visita.containsKey(destinazione)) {
					visita.put(sorgente, destinazione);
				}
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		//faccio la visita - non salvo niente perché lo fa già il traversalListener
		while(it.hasNext()) {
			it.next();
		}
		
		if(!visita.containsKey(partenza) || !visita.containsKey(destinazione)) {
			return null;
		}
		
		Airport step = destinazione; 
		while (!step.equals(partenza)) {
			percorso.add(step);
			step = visita.get(step);
		}
		percorso.add(step);
		return percorso;
	}
}
