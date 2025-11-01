# 📝 Changelog

Todas as mudanças notáveis do projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/pt-BR/1.0.0/),
e este projeto adere ao [Semantic Versioning](https://semver.org/lang/pt-BR/).

## [1.0.0] - 2025-11-01

### 🎉 Lançamento Inicial

Primeira versão funcional do Assistente de Cadastro de Refeições com IA.

### ✨ Adicionado

#### Core Features
- **n8n Workflow** - Pipeline completo de processamento de voz
  - Integração com Whisper API (OpenAI) para transcrição
  - Parser inteligente com GPT-4 para extração de dados
  - Validação e limpeza de JSON
  - Envio via Join API para Android
  
- **Tasker Profiles** - Automação Android
  - Profile com gatilhos de voz personalizáveis
  - Task de transcrição e envio para n8n
  - Task de recepção de JSON via Join
  - Automação de preenchimento com AutoInput
  
- **Android Service** - Serviço de Acessibilidade (Kotlin)
  - Abertura automática do Tecnonutri
  - Preenchimento automático de formulários
  - Processamento sequencial de múltiplos itens
  - Logs detalhados para debug

#### Documentação
- `README.md` - Visão geral completa do projeto
- `INSTALL.md` - Guia de instalação passo a passo
- `QUICKSTART.md` - Guia de início rápido (15 min)
- `CONTRIBUTING.md` - Diretrizes de contribuição
- `LICENSE` - Licença MIT
- READMEs específicos para cada módulo:
  - `n8n-flows/README.md` - Documentação de workflows
  - `tasker-profiles/README.md` - Documentação de perfis
  - `android-service/README.md` - Documentação do serviço
  - `assets/README.md` - Recursos visuais e diagramas

#### Configuração
- `.gitignore` - Exclusão de secrets e build artifacts
- `.env.example` - Template de variáveis de ambiente
- `exemplo_refeicao.json` - Exemplo de estrutura de dados
- `build.gradle` - Configuração do projeto Android

#### Assets
- Diagramas de arquitetura (Mermaid)
- Fluxogramas de dados
- Diagramas de estados
- Exemplos visuais de uso

### 🎯 Recursos Principais

- **Reconhecimento de Voz** - Comandos naturais em português
- **IA Parsing** - Extração inteligente de alimentos e quantidades
- **Automação UI** - Preenchimento automático no Tecnonutri
- **Multi-item** - Suporte a múltiplos alimentos em uma refeição
- **Feedback Visual** - Confirmações por voz e notificação
- **Error Handling** - Tratamento robusto de erros

### 📋 Comandos Suportados

- "Anotei [quantidade] [alimento]"
- "Registrar [tipo refeição] com [alimentos]"
- "Lançar refeição [alimentos]"

### 🔧 Tecnologias

- **Android**: Tasker, AutoInput, Join
- **Backend**: n8n (workflow automation)
- **IA**: OpenAI Whisper, GPT-4
- **Linguagens**: Kotlin, JavaScript, XML
- **Plataforma**: Android 7.0+ (API 24+)

### 📊 Performance

- Tempo médio end-to-end: 10-15 segundos
- Taxa de sucesso esperada: >90%
- Acurácia de transcrição: >95%
- Custo por refeição: ~$0.007 (OpenAI)

### 🔐 Segurança

- Variáveis de ambiente para secrets
- Comunicação HTTPS
- Nenhum armazenamento de dados sensíveis
- Permissões mínimas necessárias

### 📦 Estrutura do Projeto

```
taskers/
├── n8n-flows/          # Workflows de automação
├── tasker-profiles/    # Perfis Android
├── android-service/    # Código Kotlin
├── config/             # Configurações
├── assets/             # Recursos visuais
└── docs/               # Documentação
```

### 🧪 Testado Com

- Android 13 (Samsung Galaxy S21)
- Tasker v6.2.20
- n8n v1.15.0
- Tecnonutri v4.5.0
- OpenAI API (Whisper + GPT-4)

### 🐛 Problemas Conhecidos

Nenhum problema crítico conhecido nesta versão.

### 🚀 Roadmap

Planejado para próximas versões:

#### v1.1.0 (Planejado)
- [ ] Suporte a fotos de refeições
- [ ] Interface de confirmação visual
- [ ] Modo offline com cache local
- [ ] Integração com Google Fit

#### v1.2.0 (Planejado)
- [ ] Suporte a MyFitnessPal e FatSecret
- [ ] Dashboard de analytics
- [ ] Tradução para inglês e espanhol
- [ ] Treinamento personalizado de IA

#### v2.0.0 (Futuro)
- [ ] API própria para integração
- [ ] App standalone (sem Tasker)
- [ ] Suporte a iOS
- [ ] Machine Learning local

### 📝 Notas de Migração

Esta é a primeira versão, não há migração necessária.

### 🙏 Agradecimentos

- Comunidade Tasker por plugins incríveis
- OpenAI pela API acessível
- Desenvolvedores do n8n
- Todos os beta testers

### 📄 Licença

Este projeto está sob a licença MIT - veja [LICENSE](LICENSE) para detalhes.

---

## Formato de Versionamento

### [X.Y.Z]
- **X (Major)**: Mudanças incompatíveis com versões anteriores
- **Y (Minor)**: Novas funcionalidades compatíveis
- **Z (Patch)**: Correções de bugs

### Categorias
- **✨ Adicionado**: Novas features
- **🔧 Alterado**: Mudanças em features existentes
- **🗑️ Depreciado**: Features que serão removidas
- **❌ Removido**: Features removidas
- **🐛 Corrigido**: Correções de bugs
- **🔐 Segurança**: Correções de vulnerabilidades

---

**Última atualização**: 2025-11-01  
**Mantenedor**: Felipe Felix Novaes
