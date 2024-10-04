# Explodir Lotes Action Button

## Sumário

- [Descrição](#descrição)
- [Problema que Resolve](#problema-que-resolve)
- [Funcionalidades](#funcionalidades)
- [Regras de Negócio](#regras-de-negócio)
- [Como Funciona](#como-funciona)
- [Instalação](#instalação)
- [Uso](#uso)
- [Considerações Importantes](#considerações-importantes)
- [Contribuição](#contribuição)
- [Licença](#licença)
- [Contato](#contato)

## Descrição

O **Explodir Lotes Action Button** é uma extensão desenvolvida para o sistema ERP Sankhya, destinada a otimizar o processo de venda de produtos com controle de lote. Ele automatiza a divisão de itens em múltiplos lotes disponíveis no estoque, garantindo que a quantidade vendida seja atendida utilizando os lotes existentes, respeitando as regras de negócio e mantendo a integridade dos dados fiscais e financeiros.

## Problema que Resolve

No processo de vendas, é comum que um produto controlado por lote precise ser separado em diferentes lotes para atender à quantidade total solicitada pelo cliente. Manualmente, esse processo é propenso a erros e demanda tempo, além de poder causar inconsistências nos dados fiscais e de estoque. O **Explodir Lotes Action Button** automatiza essa tarefa, garantindo que os lotes sejam selecionados corretamente e que todas as informações fiscais e financeiras sejam atualizadas de forma consistente.

## Funcionalidades

- **Divisão Automática de Itens**: Divide um item de pedido em múltiplas linhas, correspondendo aos diferentes lotes disponíveis para o produto.

- **Seleção Inteligente de Lotes**: Seleciona os lotes com base na disponibilidade em estoque e na demanda do pedido, priorizando lotes com datas de validade mais próximas.

- **Atualização de Impostos**: Recalcula os impostos após a explosão dos lotes, assegurando que todos os valores estejam corretos, incluindo IPI.

- **Refazimento Financeiro**: Atualiza os lançamentos financeiros associados ao pedido, mantendo a consistência dos dados financeiros.

- **Manutenção de Vinculações**: Preserva as ligações com tabelas importantes, como a TGFVAR, evitando que o pedido fique pendente ou perca informações críticas.

## Regras de Negócio

- **Controle de Lote Obrigatório**: O produto deve possuir controle de lote para que a ação seja executada.

- **Estoque Disponível**: Somente lotes com quantidade disponível em estoque serão considerados na explosão.

- **Prioridade de Lotes**: Lotes com datas de validade mais próximas são priorizados para atender à demanda.

- **Quantidade Demandada**: A soma das quantidades dos lotes selecionados deve atender à quantidade total solicitada no pedido.

- **Atualização de Impostos**: Após a alteração dos itens, todos os impostos, incluindo IPI, devem ser recalculados corretamente.

## Como Funciona

1. **Identificação do Pedido**: O action button identifica o pedido e os itens selecionados para processamento.

2. **Consulta de Lotes Disponíveis**: Verifica os lotes disponíveis em estoque para o produto, considerando a empresa e o local de estoque.

3. **Cálculo da Demanda**: Determina a quantidade total necessária para atender ao pedido.

4. **Explosão dos Lotes**: Divide o item original em múltiplas linhas, cada uma correspondendo a um lote disponível até atender à quantidade demandada.

5. **Atualização dos Registros**: Remove a linha original do item, cria novas linhas para cada lote e atualiza os registros fiscais e financeiros associados.

6. **Recalculo de Impostos e Financeiro**: Recalcula os impostos e refaz os lançamentos financeiros para refletir as mudanças realizadas.

## Instalação

1. **Pré-requisitos**:
    - Ambiente de desenvolvimento compatível com Kotlin e as bibliotecas utilizadas.
    - Acesso ao sistema ERP Sankhya com permissões adequadas.

2. **Passos de Instalação**:
    - Clone ou faça o download do repositório do projeto.
    - Compile o código fonte utilizando seu ambiente de desenvolvimento.
    - Gere o pacote ou arquivo executável conforme os padrões do Sankhya.
    - Importe o pacote para o Sankhya utilizando as ferramentas de customização do sistema.
    - Configure o action button na interface do Sankhya, associando-o às rotinas necessárias.

## Uso

1. **Acesso ao Sistema**: Faça login no ERP Sankhya com um usuário que possua permissões para executar o action button.

2. **Seleção do Pedido**: Navegue até o módulo de vendas e selecione o pedido que deseja processar.

3. **Execução do Action Button**: Com o pedido selecionado, acione o **Explodir Lotes Action Button**.

4. **Confirmação e Processamento**: Confirme a execução da ação. O sistema processará a explosão dos lotes conforme as regras definidas.

5. **Verificação**: Após a execução, verifique se os itens foram divididos corretamente e se os impostos e lançamentos financeiros foram atualizados.

## Contribuição

Contribuições para melhorar o **Explodir Lotes Action Button** são bem-vindas. Sinta-se à vontade para abrir issues ou pull requests no repositório do projeto.

## Contato

Para dúvidas ou suporte, entre em contato com o time de desenvolvimento.
