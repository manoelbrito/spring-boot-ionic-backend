package com.manoelbrito.cursomc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.manoelbrito.cursomc.domain.Cidade;
import com.manoelbrito.cursomc.domain.Cliente;
import com.manoelbrito.cursomc.domain.Endereco;
import com.manoelbrito.cursomc.domain.enums.Perfil;
import com.manoelbrito.cursomc.domain.enums.TipoCliente;
import com.manoelbrito.cursomc.dto.ClienteDTO;
import com.manoelbrito.cursomc.dto.ClienteNewDTO;
import com.manoelbrito.cursomc.repositories.ClienteRepository;
import com.manoelbrito.cursomc.repositories.EnderecoRepository;
import com.manoelbrito.cursomc.security.UserSS;
import com.manoelbrito.cursomc.services.exception.AuthorizationException;
import com.manoelbrito.cursomc.services.exception.DataIntegrityException;
import com.manoelbrito.cursomc.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;

	public Cliente find(Integer id) {
		UserSS user=UserService.autenticated();
		if(user==null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())){
			throw new AuthorizationException("Acesso negado");
		}
		Optional<Cliente> obj=repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
		
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj=repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
public Cliente update(Cliente obj) {
		
		Cliente newObj =find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	

	public void delete(Integer id) {
		find(id);
		try {
		repo.deleteById(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionadas");
		}
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
		
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest=PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
		
	}
	
	public Cliente fromDTO(ClienteNewDTO objNewDto) {
		 Cliente cli=new Cliente(null, objNewDto.getNome(), objNewDto.getEmail(), objNewDto.getCpfOuCnpj(), TipoCliente.toEnum(objNewDto.getTipo()), pe.encode(objNewDto.getSenha()));
		 Cidade cid= new  Cidade(objNewDto.getCidadeId(), null, null);
		 Endereco end=new Endereco(null,objNewDto.getLogradouro(), objNewDto.getNumero(), objNewDto.getComplemento(), objNewDto.getBairro(), objNewDto.getCep(), cli, cid );
		 cli.getEnderecos().add(end);
		 cli.getTelefones().add(objNewDto.getTelefone1());
		 
		 if(objNewDto.getTelefone2()!=null) {
			 cli.getTelefones().add(objNewDto.getTelefone2()); 
		 }
		 if(objNewDto.getTelefone3()!=null) {
			 cli.getTelefones().add(objNewDto.getTelefone3()); 
		 }
		 return cli;
	}	
	
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
		
	}
}
