package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {

	private List<Season> stagioni;
	private FormulaOneDAO dao;
	private SimpleDirectedWeightedGraph <Driver, DefaultWeightedEdge>grafo;
	private int k;
	private List<Driver> best;
	private int minTaxSconfitta;
	
	
	public Model() {
		dao= new FormulaOneDAO();
		
	}

	public List<Season> getSeason(){
		if(stagioni==null){
			stagioni= dao.getAllSeasons();
		}
		return stagioni;
	}


	public void creaGrafo(Season season) {
		// TODO Auto-generated method stub
		grafo= new SimpleDirectedWeightedGraph<Driver, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		int anno=Integer.parseInt(season.getYear().toString());
		Graphs.addAllVertices(grafo, dao.getDriverForSeason( Integer.parseInt(season.getYear().toString())));
		for(Driver d: grafo.vertexSet()){
			dao.addResults(d, Integer.parseInt(season.getYear().toString()) );
		}
		
			for(Driver d1: grafo.vertexSet()){
				for(Driver d2: grafo.vertexSet()){
					if(!d1.equals(d2)){
						int peso=this.getPeso(d1,d2,anno);
						if(peso>0){
							DefaultWeightedEdge e=grafo.addEdge(d1, d2);
							grafo.setEdgeWeight(e, peso);
						}
					}
				}
			}
		
		System.out.println(grafo);
		
		
	}
	private int getPeso(Driver d1, Driver d2, int anno) {
		int peso=0;
		for(int i: dao.getRacesForYear(anno)){
			if(d1.getResult(i)<d2.getResult(i) && d2.getResult(i)>0 && d1.getResult(i)>0){
				peso++;
			}
		}
		return peso;
	}

	public Driver getBestDriver(){
		Driver best = null;
		int max=0;
		for(Driver d: grafo.vertexSet()){
			int peso=0;
			for(DefaultWeightedEdge e: grafo.outgoingEdgesOf(d)){
				peso+=grafo.getEdgeWeight(e);
			}
			for(DefaultWeightedEdge e: grafo.incomingEdgesOf(d)){
				peso-=grafo.getEdgeWeight(e);
			}
			if(peso>max){
				max=peso;
				best=d;
			}
		}
		return best;
	}

	public List<Driver> trovaTeam(int k) {
		// TODO Auto-generated method stub
		this.k=k;
		this.minTaxSconfitta=Integer.MAX_VALUE;
		best=new ArrayList<Driver>();
		List<Driver> parziale= new ArrayList<Driver>();
		recursive(parziale,0);
		
		return best;
	}

	private void recursive(List<Driver> parziale, int livello) {
		// TODO Auto-generated method stub
		if(parziale.size()==k){
			
			int temp=check(parziale);
			if(temp<minTaxSconfitta){
				this.minTaxSconfitta=temp;
				best.clear();
				best.addAll(parziale);
				System.out.println(best+" "+minTaxSconfitta);
				return;
				
			}
		}else{
		for(Driver d: grafo.vertexSet()){
			if(!parziale.contains(d)){
				parziale.add(d);
				recursive(parziale,livello+1);
				parziale.remove(d);
			}
		}}
		
	}

	private int check(List<Driver> parziale) {
		int tassoSconfitte=0;
		for(Driver d: parziale){
			for(Driver d1: grafo.vertexSet()){
				if(!parziale.contains(d1)){
					DefaultWeightedEdge e= grafo.getEdge(d1, d);
					if(e!=null){
					tassoSconfitte+=grafo.getEdgeWeight(e);
				}}
			}
		}
		return tassoSconfitte;
	}

}
