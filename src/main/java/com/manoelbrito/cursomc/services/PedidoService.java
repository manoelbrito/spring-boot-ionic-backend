package com.manoelbrito.cursomc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manoelbrito.cursomc.domain.ItemPedido;
import com.manoelbrito.cursomc.domain.PagamentoComBoleto;
import com.manoelbrito.cursomc.domain.Pedido;
import com.manoelbrito.cursomc.domain.enums.EstadoPagamento;
import com.manoelbrito.cursomc.repositories.ItemPedidoRepository;
import com.manoelbrito.cursomc.repositories.PagamentoRepository;
import com.manoelbrito.cursomc.repositories.PedidoRepository;
import com.manoelbrito.cursomc.repositories.ProdutoRepository;
import com.manoelbrito.cursomc.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;

	public Pedido find(Integer id) {
		
		Optional<Pedido> obj=repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
		
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstate(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pgto=(PagamentoComBoleto)obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pgto, obj.getInstate());
			
		}
		obj=repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for(ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoRepository.findById(ip.getProduto().getId()).get().getPreco());
			ip.setPedido(obj);
			
			
			
		}
		itemPedidoRepository.saveAll(obj.getItens());
		return obj;
	}
}
