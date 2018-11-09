package com.manoelbrito.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manoelbrito.cursomc.domain.Produto;
import com.manoelbrito.cursomc.dto.ProdutoDTO;
import com.manoelbrito.cursomc.resources.utils.URL;
import com.manoelbrito.cursomc.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	public ProdutoService service;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {
		Produto obj = service.find(id);

		return ResponseEntity.ok().body(obj);
	}
	
	@RequestMapping( method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value="page", defaultValue="0")Integer page,
			@RequestParam(value="nome", defaultValue="")String nome,
			@RequestParam(value="categorias", defaultValue="")String categorias,
			@RequestParam(value="linesPerPage", defaultValue="24")Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome")String orderBy,
			@RequestParam(value="direction", defaultValue="ASC")String direction) {
		//Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		//Page<CategoriaDTO>listDto=list.map(obj-> new CategoriaDTO(obj));
		String nomeDecoded=URL.decodeParam(nome);
		List<Integer> ids=URL.decodIntList(categorias);
		return ResponseEntity.ok().body(service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction).map(obj-> new ProdutoDTO(obj)));
	}
}
