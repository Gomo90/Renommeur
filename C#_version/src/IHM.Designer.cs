using System.Windows.Forms;
namespace Renommeur
{
    partial class IHM
    {
       
        /// <summary>
        /// Variable nécessaire au concepteur.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Nettoyage des ressources utilisées.
        /// </summary>
        /// <param name="disposing">true si les ressources managées doivent être supprimées ; sinon, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Code généré par le Concepteur Windows Form

        /// <summary>
        /// Méthode requise pour la prise en charge du concepteur - ne modifiez pas
        /// le contenu de cette méthode avec l'éditeur de code.
        /// </summary>
        private void InitializeComponent()
        {
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.fichierToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.menuItemChargerFichiers = new System.Windows.Forms.ToolStripMenuItem();
            this.menuItemViderGrille = new System.Windows.Forms.ToolStripMenuItem();
            this.menuItemQuitter = new System.Windows.Forms.ToolStripMenuItem();
            this.découperToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.menuItemDécouperFichiers = new System.Windows.Forms.ToolStripMenuItem();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.Conteneur = new System.Windows.Forms.TableLayoutPanel();
            this.grilleAffichage = new System.Windows.Forms.DataGridView();
            this.Column1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.Column2 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.btnRenommer = new System.Windows.Forms.Button();
            this.menuStrip1.SuspendLayout();
            this.Conteneur.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.grilleAffichage)).BeginInit();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fichierToolStripMenuItem,
            this.découperToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Size = new System.Drawing.Size(300, 24);
            this.menuStrip1.TabIndex = 0;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // fichierToolStripMenuItem
            // 
            this.fichierToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.menuItemChargerFichiers,
            this.menuItemViderGrille,
            this.menuItemQuitter});
            this.fichierToolStripMenuItem.Name = "fichierToolStripMenuItem";
            this.fichierToolStripMenuItem.Size = new System.Drawing.Size(54, 20);
            this.fichierToolStripMenuItem.Text = "Fichier";
            // 
            // menuItemChargerFichiers
            // 
            this.menuItemChargerFichiers.Name = "menuItemChargerFichiers";
            this.menuItemChargerFichiers.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.F)));
            this.menuItemChargerFichiers.Size = new System.Drawing.Size(218, 22);
            this.menuItemChargerFichiers.Text = "Charger des fichiers";
            this.menuItemChargerFichiers.Click += new System.EventHandler(this.menuItemChargerFichiers_Click);
            // 
            // menuItemViderGrille
            // 
            this.menuItemViderGrille.Enabled = false;
            this.menuItemViderGrille.Name = "menuItemViderGrille";
            this.menuItemViderGrille.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.G)));
            this.menuItemViderGrille.Size = new System.Drawing.Size(218, 22);
            this.menuItemViderGrille.Text = "Vider la grille";
            this.menuItemViderGrille.Click += new System.EventHandler(this.menuItemViderGrille_Click);
            // 
            // menuItemQuitter
            // 
            this.menuItemQuitter.Name = "menuItemQuitter";
            this.menuItemQuitter.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Q)));
            this.menuItemQuitter.Size = new System.Drawing.Size(218, 22);
            this.menuItemQuitter.Text = "Quitter";
            this.menuItemQuitter.Click += new System.EventHandler(this.menuItemQuitter_Click);
            // 
            // découperToolStripMenuItem
            // 
            this.découperToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.menuItemDécouperFichiers});
            this.découperToolStripMenuItem.Name = "découperToolStripMenuItem";
            this.découperToolStripMenuItem.Size = new System.Drawing.Size(70, 20);
            this.découperToolStripMenuItem.Text = "Découper";
            // 
            // menuItemDécouperFichiers
            // 
            this.menuItemDécouperFichiers.Enabled = false;
            this.menuItemDécouperFichiers.Name = "menuItemDécouperFichiers";
            this.menuItemDécouperFichiers.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.D)));
            this.menuItemDécouperFichiers.Size = new System.Drawing.Size(225, 22);
            this.menuItemDécouperFichiers.Text = "Découper les fichiers";
            this.menuItemDécouperFichiers.Click += new System.EventHandler(this.menuItemDécouperFichiers_Click);
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.FileName = "openFileDialog1";
            this.openFileDialog1.Filter = "All Files(*.*)|*.*";
            this.openFileDialog1.Multiselect = true;
            this.openFileDialog1.Title = "Charger les fichiers à renommer";
            // 
            // Conteneur
            // 
            this.Conteneur.ColumnCount = 1;
            this.Conteneur.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Percent, 50F));
            this.Conteneur.Controls.Add(this.grilleAffichage, 0, 0);
            this.Conteneur.Controls.Add(this.btnRenommer, 0, 1);
            this.Conteneur.Dock = System.Windows.Forms.DockStyle.Fill;
            this.Conteneur.Location = new System.Drawing.Point(0, 24);
            this.Conteneur.Name = "Conteneur";
            this.Conteneur.RowCount = 2;
            this.Conteneur.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 92.24138F));
            this.Conteneur.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 7.758621F));
            this.Conteneur.Size = new System.Drawing.Size(300, 76);
            this.Conteneur.TabIndex = 1;
            // 
            // grilleAffichage
            // 
            this.grilleAffichage.AllowUserToAddRows = false;
            this.grilleAffichage.AllowUserToDeleteRows = false;
            dataGridViewCellStyle1.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleCenter;
            dataGridViewCellStyle1.BackColor = System.Drawing.SystemColors.Control;
            dataGridViewCellStyle1.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            dataGridViewCellStyle1.ForeColor = System.Drawing.SystemColors.WindowText;
            dataGridViewCellStyle1.SelectionBackColor = System.Drawing.SystemColors.Highlight;
            dataGridViewCellStyle1.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            dataGridViewCellStyle1.WrapMode = System.Windows.Forms.DataGridViewTriState.True;
            this.grilleAffichage.ColumnHeadersDefaultCellStyle = dataGridViewCellStyle1;
            this.grilleAffichage.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.grilleAffichage.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.Column1,
            this.Column2});
            this.grilleAffichage.Dock = System.Windows.Forms.DockStyle.Fill;
            this.grilleAffichage.Location = new System.Drawing.Point(3, 3);
            this.grilleAffichage.Name = "grilleAffichage";
            this.grilleAffichage.ReadOnly = true;
            this.grilleAffichage.RowHeadersVisible = false;
            this.grilleAffichage.Size = new System.Drawing.Size(294, 64);
            this.grilleAffichage.TabIndex = 0;
            this.grilleAffichage.Visible = false;
            // 
            // Column1
            // 
            this.Column1.HeaderText = "Fichiers Chargés";
            this.Column1.Name = "Column1";
            this.Column1.ReadOnly = true;
            this.Column1.Width = 400;
            // 
            // Column2
            // 
            this.Column2.HeaderText = "Fichiers renommés";
            this.Column2.Name = "Column2";
            this.Column2.ReadOnly = true;
            this.Column2.Width = 200;
            // 
            // btnRenommer
            // 
            this.btnRenommer.Dock = System.Windows.Forms.DockStyle.Fill;
            this.btnRenommer.Location = new System.Drawing.Point(3, 73);
            this.btnRenommer.Name = "btnRenommer";
            this.btnRenommer.Size = new System.Drawing.Size(294, 1);
            this.btnRenommer.TabIndex = 1;
            this.btnRenommer.Text = "Renommer";
            this.btnRenommer.UseVisualStyleBackColor = true;
            this.btnRenommer.Visible = false;
            this.btnRenommer.Click += new System.EventHandler(this.btnRenommer_Click);
            // 
            // IHM
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(300, 100);
            this.Controls.Add(this.Conteneur);
            this.Controls.Add(this.menuStrip1);
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "IHM";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Renommeur 1.3";
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.Conteneur.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.grilleAffichage)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem fichierToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem menuItemChargerFichiers;
        private System.Windows.Forms.ToolStripMenuItem menuItemViderGrille;
        private System.Windows.Forms.ToolStripMenuItem menuItemQuitter;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.TableLayoutPanel Conteneur;
        private System.Windows.Forms.DataGridView grilleAffichage;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column1;
        private System.Windows.Forms.DataGridViewTextBoxColumn Column2;
        private System.Windows.Forms.Button btnRenommer;
        private System.Windows.Forms.ToolStripMenuItem découperToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem menuItemDécouperFichiers;
        //public System.Windows.Forms.ProgressBar progressBar;
    }
}

