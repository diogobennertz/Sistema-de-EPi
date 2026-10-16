# Sistema de EPi

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EPI Control - Documentação</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background-color: #0d1117;
            color: #c9d1d9;
            display: flex;
            height: 100vh;
            overflow: hidden;
        }

        /* Menu Lateral */
        .sidebar {
            width: 340px;
            background-color: #161b22;
            border-right: 1px solid #30363d;
            padding: 24px 20px;
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .sidebar h1 {
            font-size: 1.4rem;
            color: #f0f6fc;
            padding-bottom: 12px;
            border-bottom: 1px solid #30363d;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .pdf-list {
            list-style: none;
            display: flex;
            flex-direction: column;
            gap: 10px;
            overflow-y: auto;
        }

        .pdf-item a {
            display: block;
            color: #58a6ff;
            text-decoration: none;
            padding: 12px;
            border-radius: 6px;
            background-color: #21262d;
            border: 1px solid #30363d;
            font-size: 0.85rem;
            font-family: monospace;
            transition: all 0.2s ease;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .pdf-item a:hover, .pdf-item.active a {
            background-color: #30363d;
            color: #79c0ff;
            border-color: #8b949e;
        }

        /* Área de Conteúdo */
        .viewer-container {
            flex: 1;
            display: flex;
            flex-direction: column;
            background-color: #0d1117;
        }

        .viewer-header {
            padding: 18px 24px;
            background-color: #161b22;
            border-bottom: 1px solid #30363d;
        }

        .viewer-header h2 {
            font-size: 1.1rem;
            color: #8b949e;
        }

        .embed-wrapper {
            flex: 1;
            padding: 20px;
            background-color: #0d1117;
        }

        embed {
            width: 100%;
            height: 100%;
            border-radius: 8px;
            border: 1px solid #30363d;
            background-color: #1f242c;
        }
    </style>
</head>
<body>

    <!-- Menu Lateral com a sua lista real de arquivos -->
    <div class="sidebar">
        <h1>Sistema de EPi</h1>
        <ul class="pdf-list">
            <li class="pdf-item active"><a href="01_regras_de_negocio.pdf" target="pdf_viewer">01_regras_de_negocio.pdf</a></li>
            <li class="pdf-item"><a href="02_requisitos_funcionais.pdf" target="pdf_viewer">02_requisitos_funcionais.pdf</a></li>
            <li class="pdf-item"><a href="03_requisitos_nao_funcionais.pdf" target="pdf_viewer">03_requisitos_nao_funcionais.pdf</a></li>
            <li class="pdf-item"><a href="04_diagrama_de_casos_de_uso.pdf" target="pdf_viewer">04_diagrama_de_casos_de_uso.pdf</a></li>
            <li class="pdf-item"><a href="05_diagrama_de_classes.pdf" target="pdf_viewer">05_diagrama_de_classes.pdf</a></li>
            <li class="pdf-item"><a href="06_diagrama_funcionamento_do_sistema.pdf" target="pdf_viewer">06_diagrama_funcionamento_do_sistema.pdf</a></li>
        </ul>
    </div>

    <!-- Painel de Visualização das Imagens/PDFs -->
    <div class="viewer-container">
        <div class="viewer-header">
            <h2 id="doc-title">01_regras_de_negocio.pdf</h2>
        </div>
        <div class="embed-wrapper">
            <!-- Renderiza o primeiro arquivo imediatamente na tela -->
            <embed src="01_regras_de_negocio.pdf" type="application/pdf" name="pdf_viewer" id="pdf-embed" />
        </div>
    </div>

    <script>
        const items = document.querySelectorAll('.pdf-item');
        const docTitle = document.getElementById('doc-title');

        items.forEach(item => {
            const link = item.querySelector('a');
            link.addEventListener('click', function() {
                // Atualiza a classe ativa no menu lateral
                items.forEach(i => i.classList.remove('active'));
                item.classList.add('active');
                
                // Muda o cabeçalho dinamicamente
                docTitle.textContent = this.textContent;
            });
        });
    </script>
</body>
</html>
