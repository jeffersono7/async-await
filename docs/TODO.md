# Lista de funcionalidades/implementações para fazer
- 
- CD (Maven repository)
- Supervisors como um tipo de processo que gerencia outros
- Publicação no maven repository - DONE
- Deep copy das mensagens, para tornar um pouco mais pura as funções dos processos
- Cobertura de testes - DONE
- CI - DONE
- Trocar algumas assinaturas, tipo spawn, ... (Ficar mais próximo ao Elixir) - DONE
- Morte de um processo - DONE
- Controle de excessões, principalmente para não comprometer "infra" do async/await - DONE

### Para discutir

- Talvez seja interessante o source ser sempre o pid de quem fez despatch, e não poder dizer quem foi.
    - No elixir ainda é possível informar o pid de retorno, então não faz sentido por este block.
