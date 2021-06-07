# Lista de funcionalidades/implementações para fazer

- Cobertura de testes - DONE
- CI - DONE
- CD (Maven repository)
- Trocar algumas assinaturas, tipo spawn, ... (Ficar mais próximo ao Elixir) - DONE
- Morte de um processo - DONE
- Supervisors como um tipo de processo que gerencia outros
- Controle de excessões, principalmente para não comprometer "infra" do async/await - DONE
- Publicação no maven repository
- Deep copy das mensagens, para tornar um pouco mais pura as funções dos processos

### Para discutir

- Talvez seja interessante o source ser sempre o pid de quem fez despatch, e não poder dizer quem foi.
